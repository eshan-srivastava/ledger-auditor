package db

const (
	create_txn = `
	INSERT INTO transactions
	(amount,createdAt,sourceAccId,destAccId,noteString,checksum)
	VALUES
	(:amount,:createdAt,:sourceAccId,:destAccId,:noteString,:checksum)	
	`

	select_1_txn = `
	SELECT id,amount,createdAt,sourceAccId,destAccId,noteString,checksum
	FROM transactions WHERE
	id = ?
	`

	select_n_txn = `
	SELECT id,amount,createdAt,sourceAccId,destAccId,noteString,checksum
	FROM transactions WHERE
	id IN (?)
	`
)
