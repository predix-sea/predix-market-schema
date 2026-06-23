package com.predix.marketschema.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.domain.enums.ResolutionSource;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
class MarketLifecycleIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullLifecycle_createOpenCloseResolveSettle() throws Exception {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        String createBody = """
                {
                  "title": "Will BTC exceed 100k?",
                  "description": "Binary market",
                  "category": "crypto",
                  "marketType": "BINARY",
                  "chainId": 137,
                  "collateralTokenSymbol": "USDC",
                  "openTime": "%s",
                  "closeTime": "%s",
                  "resolveDeadline": "%s",
                  "createdBy": "ops@predix.io"
                }
                """.formatted(now.plusHours(1), now.plusDays(1), now.plusDays(2));

        MvcResult createResult = mockMvc.perform(post("/api/v1/markets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andReturn();

        UUID marketId = UUID.fromString(readData(createResult).get("id").asText());

        MvcResult yesOutcome = mockMvc.perform(post("/api/v1/markets/{id}/outcomes", marketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"outcomeCode":"YES","outcomeLabel":"Yes"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        UUID yesOutcomeId = UUID.fromString(readData(yesOutcome).get("id").asText());

        mockMvc.perform(post("/api/v1/markets/{id}/outcomes", marketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"outcomeCode":"NO","outcomeLabel":"No"}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/markets/{id}/open", marketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"actor\":\"ops@predix.io\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("OPEN"));

        mockMvc.perform(post("/api/v1/markets/{id}/close", marketId)
                        .content("{\"actor\":\"ops@predix.io\"}"))
                .andExpect(jsonPath("$.data.status").value("CLOSED"));

        mockMvc.perform(post("/api/v1/markets/{id}/start-resolving", marketId)
                        .content("{\"actor\":\"oracle-bot\"}"))
                .andExpect(jsonPath("$.data.status").value("RESOLVING"));

        mockMvc.perform(post("/api/v1/markets/{id}/resolution-records", marketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "resolutionSource": "UMA",
                                  "umaRequestTxHash": "0xabc",
                                  "proposedOutcomeCode": "YES",
                                  "finalOutcomeCode": "YES",
                                  "resolverRef": "oracle-bot"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/markets/{id}/resolve", marketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"winningOutcomeCode":"YES","actor":"oracle-bot"}
                                """))
                .andExpect(jsonPath("$.data.status").value("RESOLVED"));

        mockMvc.perform(post("/api/v1/markets/{id}/settlements", marketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "outcomeId": "%s",
                                  "userId": "user-1",
                                  "redeemQuantity": "10",
                                  "payoutAmount": "10",
                                  "payoutToken": "USDC"
                                }
                                """.formatted(yesOutcomeId)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/markets/{id}/settle", marketId)
                        .content("{\"actor\":\"settlement-bot\"}"))
                .andExpect(jsonPath("$.data.status").value("SETTLED"));

        mockMvc.perform(get("/api/v1/markets/{id}", marketId))
                .andExpect(jsonPath("$.data.status").value("SETTLED"));

        mockMvc.perform(get("/api/v1/markets/{id}/resolution-records", marketId))
                .andExpect(jsonPath("$.data.length()").value(1));

        mockMvc.perform(patch("/api/v1/outcomes/{id}", yesOutcomeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"outcomeLabel\":\"Yes winner\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("2005"));
    }

    @Test
    void invalidTransition_returnsBusinessError() throws Exception {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        MvcResult result = mockMvc.perform(post("/api/v1/markets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Draft only",
                                  "category": "test",
                                  "chainId": 137,
                                  "collateralTokenSymbol": "USDC",
                                  "openTime": "%s",
                                  "closeTime": "%s",
                                  "resolveDeadline": "%s",
                                  "createdBy": "tester"
                                }
                                """.formatted(now.plusHours(1), now.plusDays(1), now.plusDays(2))))
                .andExpect(status().isCreated())
                .andReturn();

        UUID marketId = UUID.fromString(readData(result).get("id").asText());

        mockMvc.perform(post("/api/v1/markets/{id}/close", marketId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("2001"));
    }

    @Test
    void orderOnOpenMarket_persistsPosition() throws Exception {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        MvcResult marketResult = mockMvc.perform(post("/api/v1/markets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Order test",
                                  "category": "test",
                                  "chainId": 137,
                                  "collateralTokenSymbol": "USDC",
                                  "openTime": "%s",
                                  "closeTime": "%s",
                                  "resolveDeadline": "%s",
                                  "createdBy": "tester"
                                }
                                """.formatted(now.plusMinutes(1), now.plusDays(1), now.plusDays(2))))
                .andReturn();

        UUID marketId = UUID.fromString(readData(marketResult).get("id").asText());

        MvcResult yes = mockMvc.perform(post("/api/v1/markets/{id}/outcomes", marketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"outcomeCode\":\"YES\",\"outcomeLabel\":\"Yes\"}"))
                .andReturn();
        UUID outcomeId = UUID.fromString(readData(yes).get("id").asText());

        mockMvc.perform(post("/api/v1/markets/{id}/outcomes", marketId)
                        .content("{\"outcomeCode\":\"NO\",\"outcomeLabel\":\"No\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/markets/{id}/open", marketId).content("{}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "marketId": "%s",
                                  "outcomeId": "%s",
                                  "side": "BUY",
                                  "orderType": "LIMIT",
                                  "price": "0.55",
                                  "quantity": "100",
                                  "userId": "trader-1"
                                }
                                """.formatted(marketId, outcomeId)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/positions").param("marketId", marketId.toString()).param("userId", "trader-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].quantity").value(100));

        assertThat(MarketStatus.OPEN.name()).isNotBlank();
    }

    private JsonNode readData(MvcResult result) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("data");
    }
}
