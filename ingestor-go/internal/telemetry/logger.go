package telemetry

import (
	"net/http"
	"sync"
	"time"

	"github.com/go-chi/chi/v5/middleware"
	"go.uber.org/zap"
)

var (
	loggerOnce sync.Once
	restorer   func()
)

func InitGlobalLogger() error {
	var err error
	loggerOnce.Do(func() {
		l, e := zap.NewProduction()
		if e != nil {
			err = e
			return
		}
		restorer = zap.ReplaceGlobals(l)
	})
	return err
}

func GetGlobalLogger() *zap.Logger {
	return zap.L()
}

// ZapLogger is a middleware that logs structured HTTP request records using Zap
func ZapLogger() func(next http.Handler) http.Handler {
	log := zap.L()
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			// Wrap the original ResponseWriter to capture the HTTP status code
			ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)

			// Execute the rest of the application middleware/handler chain
			next.ServeHTTP(ww, r)

			// Calculate processing latency
			duration := time.Since(start)

			// Construct structured log fields
			fields := []zap.Field{
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.Int("status", ww.Status()),
				zap.Duration("duration", duration),
				zap.String("remote_ip", r.RemoteAddr),
				zap.Int("bytes_written", ww.BytesWritten()),
			}

			// Capture Chi's request ID if it is available in context
			if reqID := middleware.GetReqID(r.Context()); reqID != "" {
				fields = append(fields, zap.String("request_id", reqID))
			}

			// Route severity logs based on the returned HTTP response code
			if ww.Status() >= 500 {
				log.Error("HTTP Request Failed", fields...)
			} else if ww.Status() >= 400 {
				log.Warn("HTTP Request Warning", fields...)
			} else {
				log.Info("HTTP Request Success", fields...)
			}
		})
	}
}
