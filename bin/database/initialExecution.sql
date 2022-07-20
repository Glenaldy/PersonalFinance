DROP TABLE IF EXISTS wallet_balance;

DROP TABLE IF EXISTS transactions;

DROP TABLE IF EXISTS wallets;

DROP TABLE IF EXISTS currency;

CREATE TABLE currency (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    currency_name STRING UNIQUE
);

CREATE TABLE transactions(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    currency STRING NOT NULL,
    amount INTEGER NOT NULL,
    category STRING,
    description STRING,
    trans_date DATE NOT NULL,
    parent_id INTEGER,
    critical BOOLEAN DEFAULT false,
    paid BOOLEAN DEFAULT true,
    FOREIGN KEY (parent_id) REFERENCES transactions(id) ON DELETE CASCADE
);

CREATE TABLE wallets(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    currency STRING NOT NULL,
    wallet_name STRING NOT NULL,
    CONSTRAINT wallets_currency FOREIGN KEY (currency) REFERENCES currency(currency_name) ON DELETE CASCADE UNIQUE (currency, wallet_name)
);

CREATE TABLE wallet_balance(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    record_currency STRING,
    record_wallet STRING,
    record_date DATE NOT NULL,
    amount INTEGER NOT NULL,
    CONSTRAINT wallet_name_currency_balance FOREIGN KEY(record_currency, record_wallet) REFERENCES wallets(currency, wallet_name)
);

INSERT INTO
    currency (currency_name)
VALUES
    ("yen"),
    ("rupiah"),
    ("usd");

INSERT INTO
    wallets (currency, wallet_name)
VALUES
    ("yen", "wallet");

INSERT INTO
    wallets (currency, wallet_name)
VALUES
    ("yen", "yuucho");

INSERT INTO
    wallets (currency, wallet_name)
VALUES
    ("yen", "paypal");

INSERT INTO
    wallet_balance(
        record_currency,
        record_wallet,
        record_date,
        amount
    )
VALUES
    ("yen", "wallet", "2022/07/01", 1000),
    ("yen", "wallet", "2022/07/10", 8000),
    ("yen", "wallet", "2022/07/20", 5000),
    ("yen", "yuucho", "2022/07/01", 10000),
    ("yen", "yuucho", "2022/07/20", 50000),
    ("yen", "paypal", "2022/07/01", 10000),
    ("yen", "paypal", "2022/07/01", 50000);

INSERT INTO
    transactions (
        id,
        currency,
        amount,
        category,
        description,
        trans_date
    )
VALUES
    (
        1,
        "yen",
        5000,
        "conbini",
        "parent transaction",
        "2022/07/18"
    ),
    (
        2,
        "yen",
        5000,
        "another conbini",
        "parent transaction",
        "2022/07/18"
    );

INSERT INTO
    transactions (
        currency,
        amount,
        category,
        description,
        trans_date,
        parent_id
    )
VALUES
    (
        "yen",
        1000,
        "conbini",
        "child transaction",
        "2022/07/18",
        1
    ),
    (
        "yen",
        1000,
        "conbini",
        "child transaction",
        "2022/07/18",
        2
    ),
    (
        "yen",
        1000,
        "conbini",
        "child transaction",
        "2022/07/18",
        1
    ),
    (
        "yen",
        1000,
        "conbini",
        "child transaction",
        "2022/07/18",
        2
    ),
    (
        "yen",
        1000,
        "conbini",
        "child transaction",
        "2022/07/18",
        2
    );