DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS wallets;

CREATE TABLE transactions(
    id INTEGER PRIMARY KEY AUTOINCREMENT ,
	currency STRING NOT NULL,
    amount INTEGER NOT NULL,
    category STRING,
    description STRING,
    trans_date DATE,
	super_id INTEGER,
	FOREIGN KEY (super_id) REFERENCES transactions(id)
);

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
        "supermarket",
        "super transaction",
        "2022/07/18"
    );
	
	
INSERT INTO
    transactions (
        currency,
        amount,
        category,
        description,
        trans_date,
		super_id
    )
VALUES
    (
        "yen",
        1000,
        "supermarket",
        "sub transaction",
        "2022/07/18",
		1
    ),
    (
        "yen",
        1000,
        "supermarket",
        "sub transaction",
        "2022/07/18",
		1
    ),
    (
        "yen",
        1000,
        "supermarket",
        "sub transaction",
        "2022/07/18",
		1
    ),
    (
        "yen",
        1000,
        "supermarket",
        "sub transaction",
        "2022/07/18",
		1
    )	
	;
	
SELECT super.id AS super_transaction, super.amount AS total, sum(sub.amount) AS sub_transaction_total
FROM transactions AS super 
JOIN transactions AS sub 
ON super.id = sub.super_id
GROUP BY super.id;

SELECT super.id AS parent_id, 
	COUNT(sub.id) AS count_sub_transaction, 
	super.amount AS parent_total_transaction,
	SUM(sub.amount) AS child_total_transaction,
	(super.amount - SUM(sub.amount)) AS unlabeled_amount
FROM transactions AS super 
JOIN transactions AS sub 
ON super.id = sub.super_id;











