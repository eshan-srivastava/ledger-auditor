package models

import "errors"

var (
	ErrFailedToPoll = errors.New("failed to poll latest data")
)
