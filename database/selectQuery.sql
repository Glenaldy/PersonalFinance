SELECT parent.id AS parent_transaction, parent.amount AS total, sum(child.amount) AS child_transaction_total
FROM transactions AS parent 
JOIN transactions AS child 
ON parent.id = child.parent_id
GROUP BY parent.id;

SELECT parent.id AS parent_id, 
	parent.category AS parent_category,
	parent.description AS parent_description,
	parent.amount AS parent_total_transaction,
	COUNT(child.id) AS count_child_transaction, 
	(parent.amount - SUM(child.amount)) AS unlabeled_amount
FROM transactions AS parent 
JOIN transactions AS child 
ON parent.id = child.parent_id;

SELECT parent.id AS parent_id, parent.category AS parent_category,parent.description AS parent_description,parent.amount AS parent_total_transaction, COUNT(child.id) AS count_child_transaction, (parent.amount - SUM(child.amount)) AS unlabeled_amount FROM transactions AS parent JOIN transactions AS child ON parent.id = child.parent_id;

SELECT 
	parent.id AS parent_id, parent.category AS parent_category,
	parent.description AS parent_description,
	parent.amount AS parent_total_transaction, 
	COUNT(child.id) AS count_child_transaction, 
	(parent.amount - SUM(child.amount)) AS unlabeled_amount 
FROM transactions AS parent JOIN transactions AS child
ON parent.id = child.parent_id
WHERE parent.id = 2
GROUP BY parent.id
;

SELECT
	id,
	amount,
	description
FROM transactions WHERE parent_id = 2;

SELECT child.id AS child_id, child.amount AS child_transaction_amount, child.description AS child_description FROM transactions AS child WHERE parent_id = 1;


SELECT parent.id AS parent_id, parent.category AS parent_category,parent.description AS parent_description,parent.amount AS parent_total_transaction, COUNT(child.id) AS count_child_transaction, (parent.amount - SUM(child.amount)) AS unlabeled_amount FROM transactions AS parent JOIN transactions AS child ON parent.id = child.parent_id GROUP BY parent.id;




