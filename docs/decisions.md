# Design decisions

## 1. Reference other features by id, not by JPA relationship

**Context.** Categories, transactions, budgets and goals all belong to a user, and transactions
belong to a category.

**Decision.** Entities store plain ids (`Long userId`, `Long categoryId`) instead of
`@ManyToOne` relationships. Integrity is still enforced by foreign keys in the database.

**Consequences.** Feature packages do not depend on each other's entities, and there is no lazy
loading to cause `LazyInitializationException` outside transactions. The trade-off is losing
navigation (`category.getUser()`): queries that need data from two tables use explicit joins.

## 2. Effective-dated budgets

**Context.** Budgets repeat every month until changed, but changing a budget must not alter
the dashboards of past months.

**Decision.** Each budget item is valid from a start month until an optional end month. Changing
a value closes the current item at the previous month and opens a new one. An item created in
the current month has no history yet, so it is deleted instead of closed.

**Consequences.** Past months are immutable and always show what was planned at the time. Queries
need a validity filter, and replacing an item requires an explicit `flush()`: Hibernate executes
inserts before updates and deletes, which would briefly create two active items and violate the
partial unique index.