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

	select_cursor = `
	SELECT id,last_seen_created_at,last_seen_id,updated_at
	FROM sync_state
	WHERE id = 1
	`

	set_cursor = `
	INSERT INTO sync_state
	(id,last_seen_created_at,last_seen_id,updated_at)
	VALUES
	(:)
	`
)
