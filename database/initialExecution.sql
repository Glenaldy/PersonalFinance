DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS wallets;

CREATE TABLE transactions(
    id INTEGER PRIMARY KEY,
	currency STRING,
    amount INTEGER,
    category STRING,
    description STRING,
    transDate DATE
);
CREATE TABLE wallets (
	id INTEGER PRIMARY KEY,
	currency STRING,
	walletName STRING
);

INSERT INTO
    transactions (
        id,
        currency,
        amount,
        category,
        description,
        transDate
    )
VALUES
    (
        20220718001,
        "yen",
        5000,
        "supermarket",
        "description very long",
        "2022/07/18"
    );

INSERT INTO
    transactions (
        id,
        currency,
        amount,
        category,
        description,
        transDate
    )
VALUES
    (
        20220718002,
        "yen",
        4000,
        "conbini",
        "description very long",
        "2022/07/18"
    );