package models

import "errors"

var (
	ErrFailedToPoll      = errors.New("failed to poll latest data")
	ErrCheckSumEmpty     = errors.New("checksum is empty")
	ErrChecksumMismatch  = errors.New("checksum failed")
	ErrTxnNotFound       = errors.New("no transaction found")
	ErrEmptyPollerCursor = errors.New("poller cursor not found")
)
