DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS wallets;

CREATE TABLE transactions(
    id INTEGER PRIMARY KEY AUTOINCREMENT ,
	currency STRING NOT NULL,
    amount INTEGER NOT NULL,
    category STRING,
    description STRING,
    trans_date DATE,
	parent_id INTEGER,
    critical BOOLEAN DEFAULT false,
    paid BOOLEAN DEFAULT true,
	FOREIGN KEY (parent_id) REFERENCES transactions(id)
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
    )
	;
	
