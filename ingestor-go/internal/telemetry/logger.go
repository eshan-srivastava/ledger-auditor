package telemetry

import (
	"sync"

	"go.uber.org/zap"
)

var (
	loggerOnce sync.Once
)

func InitGlobalLogger() error {
	var err error
	loggerOnce.Do(func() {
		l, e := zap.NewProduction()
		if e != nil {
			err = e
			return
		}
		zap.ReplaceGlobals(l)
	})
	return err
}
