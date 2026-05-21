# 市场生命周期与数据字典

## 状态定义

| 状态 | 含义 |
|------|------|
| DRAFT | 草稿，可编辑元数据、添加 outcome |
| OPEN | 开放交易 |
| CLOSED | 已收盘，停止下单 |
| RESOLVING | 裁决进行中（UMA/人工） |
| RESOLVED | 已确定唯一胜出 outcome |
| SETTLED | 结算完成，全表只读（outcome winning 不可改） |
| CANCELLED | 取消（仅从 OPEN 且满足条件） |

## 业务规则摘要

1. 至少 2 个 outcome；`BINARY` 类型必须恰好 `YES` + `NO`。
2. `outcome_index` 必须从 0 连续到 N-1（对齐 CTF `indexSet`）。
3. `open_time < close_time < resolve_deadline`（创建、开盘前校验）。
4. `RESOLVED` 时恰好一个 `is_winning = true`。
5. `SETTLED` 后禁止修改 outcome / winning。
6. 每次状态迁移写入 `market_audit_logs`。

## 表：markets

| 列 | 类型 | 说明 |
|----|------|------|
| id | UUID | PK |
| market_code | VARCHAR(64) UK | 如 PMKT_20260520_0001 |
| title | VARCHAR(512) | 标题 |
| description | TEXT | 描述 |
| category | VARCHAR(64) | 分类 |
| market_type | BINARY / CATEGORICAL | 市场类型 |
| chain_id | INT | 链 ID（Polygon=137） |
| collateral_token_symbol | VARCHAR(32) | 抵押代币 |
| ctf_condition_id | VARCHAR(128) | CTF 条件 ID（可空，链上回填） |
| uma_question_id | VARCHAR(128) | UMA 问题 ID（可空） |
| status | ENUM | 状态 |
| open_time / close_time / resolve_deadline | TIMESTAMPTZ | 时间窗口 |
| created_by | VARCHAR(128) | 创建人 |
| created_at / updated_at | TIMESTAMPTZ | 审计时间 |

## 表：market_outcomes

| 列 | 说明 |
|----|------|
| outcome_code | YES/NO 或 HOME/AWAY/DRAW |
| outcome_index | CTF 兼容索引 0..N-1 |
| is_winning | 裁决后设置 |
| payout_numerator / payout_denominator | 赔付比例 |

唯一约束：`(market_id, outcome_code)`、`(market_id, outcome_index)`。

## 表：orders

| 列 | 说明 |
|----|------|
| order_code | 业务单号 UK |
| side | BUY / SELL |
| order_type | LIMIT / MARKET |
| price / quantity / filled_quantity | NUMERIC |
| status | NEW / PARTIALLY_FILLED / FILLED / CANCELLED / REJECTED |

索引：`(market_id, user_id, status, created_at)`。

## 表：positions

| 列 | 说明 |
|----|------|
| quantity / avg_cost | 持仓数量与均价 |
| unrealized_pnl | 可选未实现盈亏 |

唯一：`(market_id, outcome_id, user_id)`。

## 表：resolution_records

| 列 | 说明 |
|----|------|
| resolution_source | UMA / MANUAL / ORACLE |
| uma_request_tx_hash / uma_assertion_id | UMA 链上引用 |
| proposed_outcome_code / final_outcome_code | 提议与最终结果 |
| disputed | 是否争议 |
| raw_payload | JSONB 原始事件 |

## 表：settlements

| 列 | 说明 |
|----|------|
| redeem_quantity / payout_amount | 赎回与支付 |
| payout_token | 支付代币符号 |
| settlement_tx_hash | 链上结算交易 |

## 表：market_audit_logs

记录 `from_status` → `to_status`、`action`、`actor`、`detail`。

## API 与状态操作映射

| API | 目标状态 |
|-----|----------|
| POST /markets | DRAFT |
| POST /markets/{id}/open | OPEN |
| POST /markets/{id}/close | CLOSED |
| POST /markets/{id}/start-resolving | RESOLVING |
| POST /markets/{id}/resolve | RESOLVED |
| POST /markets/{id}/settle | SETTLED |
| POST /markets/{id}/cancel | CANCELLED |

## 错误码

| code | 含义 |
|------|------|
| 0 | 成功 |
| 1001 | 参数校验失败 |
| 1002 | 资源不存在 |
| 2001 | 非法状态迁移 |
| 2002 | 市场规则违反 |
| 2003 | outcome 规则违反 |
| 2004 | 订单规则违反 |
| 2005 | 已结算不可变 |
| 9999 | 内部错误 |
