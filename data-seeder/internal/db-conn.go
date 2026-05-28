package internal

import (
	"context"
	"database/sql"
	"fmt"
	"os"
)

type PGDB struct {
	db *sql.DB
}

func GetPostgres() *PGDB {
	dbUrl := os.Getenv("PG_URL")

	db, err := sql.Open("postgres", dbUrl)
	if err != nil {
		panic("error connecting to postgres, " + err.Error())
	}

	return &PGDB{
		db: db,
	}
}

func (p *PGDB) ClearDB(ctx context.Context) error {
	tx, err := p.db.BeginTx(ctx, nil)
	if err != nil {
		return fmt.Errorf("failed to start txn: %w", err)
	}

	// Defer a rollback. If Commit() is called successfully, Rollback() does nothing.
	defer tx.Rollback()

	query := `TRUNCATE TABLE transactions, users, accounts RESTART IDENTITY CASCADE;`

	if _, err := tx.ExecContext(ctx, query); err != nil {
		return fmt.Errorf("failed to truncate tables: %w", err)
	}

	if err := tx.Commit(); err != nil {
		return fmt.Errorf("failed to commit txn: %w", err)
	}

	return nil
}
