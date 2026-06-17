package api

import (
	"net/http"

	"github.com/eshan-srivastava/ingestor-go/internal/services"
	"go.uber.org/zap"
)

type HttpHandler struct {
	ms   *services.MirrorService
	zlog *zap.Logger
}

func NewHttpHandler(
	ms *services.MirrorService,
	zlog *zap.Logger,
) *HttpHandler {
	return &HttpHandler{
		ms:   ms,
		zlog: zlog,
	}
}

func (h *HttpHandler) GetSpendsHandler(w http.ResponseWriter, r *http.Request) {

}

func (h *HttpHandler) GetTransactionsListHandler(w http.ResponseWriter, r *http.Request) {

}
