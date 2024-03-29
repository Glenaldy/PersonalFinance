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
    FOREIGN KEY (parent_id) REFERENCES transactions(id) ON DELETE CASCADE,
    FOREIGN KEY (currency) REFERENCES currency(currency_name) ON DELETE CASCADE
);

CREATE TABLE wallets(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    currency STRING NOT NULL,
    wallet_name STRING NOT NULL,
    CONSTRAINT wallets_currency FOREIGN KEY (currency) REFERENCES currency(currency_name) ON DELETE CASCADE UNIQUE (currency, wallet_name)
);

CREATE TABLE wallet_balance(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    record_wallet ID,
    record_date DATE NOT NULL,
    amount INTEGER NOT NULL,
    CONSTRAINT wallet_foreign_id FOREIGN KEY(record_wallet) REFERENCES wallets(id) ON DELETE CASCADE
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
        record_wallet,
        record_date,
        amount
    )
VALUES
    (1, "2022-07-01", 1000),
    (1, "2022-07-10", 8000),
    (1, "2022-07-20", 5000),
    (2, "2022-07-01", 10000),
    (2, "2022-07-20", 50000),
    (3, "2022-06-01", 10000),
    (3, "2022-07-01", 50000);

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
        -5000,
        "conbini",
        "parent transaction",
        "2022-07-18"
    ),
    (
        2,
        "yen",
        -5000,
        "another conbini",
        "parent transaction",
        "2022-06-18"
    ),
    (
        3,
        "yen",
        -5000,
        "another conbini",
        "parent transaction",
        "2022-08-18"
    );

INSERT INTO
    transactions (
        currency,
        amount,
        category,
        description,
        trans_date,
        parent_id,
        critical,
        paid
    )
VALUES
    (
        "yen",
        -40000,
        "rent",
        "August rent payment",
        "2022-08-25",
        null,
        true,
        false
    ),
    (
        "yen",
        -5000,
        "electricity",
        "August electricity payment",
        "2022-08-25",
        null,
        true,
        false
    ),
    (
        "yen",
        -3000,
        "gas",
        "August gas payment",
        "2022-08-25",
        null,
        true,
        false
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
        -1000,
        "conbini",
        "child transaction",
        "2022-07-18",
        1
    ),
    (
        "yen",
        -1000,
        "conbini",
        "child transaction",
        "2022-06-18",
        2
    ),
    (
        "yen",
        -1000,
        "conbini",
        "child transaction",
        "2022-07-18",
        1
    ),
    (
        "yen",
        -1000,
        "conbini",
        "child transaction",
        "2022-06-18",
        2
    ),
    (
        "yen",
        -1000,
        "conbini",
        "child transaction",
        "2022-06-18",
        2
    );