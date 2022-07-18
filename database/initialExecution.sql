DROP TABLE IF EXISTS transactions;

CREATE TABLE transactions(
    id INTEGER PRIMARY KEY,
    amount INTEGER,
    category STRING,
    description STRING,
    transDate DATE
);

INSERT INTO
    transactions (
        id,
        amount,
        category,
        description,
        transDate
    )
VALUES
    (
        20220718001,
        5000,
        "supermarket",
        "description very long",
        "2022/07/18"
    );

INSERT INTO
    transactions (
        id,
        amount,
        category,
        description,
        transDate
    )
VALUES
    (
        20220718002,
        4000,
        "conbini",
        "description very long",
        "2022/07/18"
    );