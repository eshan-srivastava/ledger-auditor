package api

import (
	"net/http"
	"strings"

	"github.com/eshan-srivastava/ingestor-go/internal/lib"
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
	idListStr := r.URL.Query().Get("ids")
	idList := strings.Split(idListStr, ",")

	if len(idList) == 0 {
		lib.JsonError(w, http.StatusBadRequest, "idList is empty")
		return
	}

	transactionList, err := h.ms.GetTransactionsByIDList(r.Context(), idList)
	if err != nil {
		h.zlog.Error("error fetching transactionlist",
			zap.String("idList", idListStr),
			zap.Error(err),
		)
		lib.JsonError(w, http.StatusInternalServerError, "error fetching transaction list")
		return
	}

	lib.JsonWriter(w, http.StatusOK, transactionList)

}

// TODO
func (h *HttpHandler) GetFilteredTransactionListHandler(w http.ResponseWriter, r *http.Request) {

}
