package ingestion

// component on mirror service
// can act as a interface implementation that regularly fetches data
type UpstreamPoller struct {
}

func (up *UpstreamPoller) Start()

func (up *UpstreamPoller) Stop()

// reads cursor from sql table, if not found then returns empty
func readCursor()

// uses the fetched cursor to hit java API
func fetchSource()

// use in conjuction with fetchPage's writer to ingest into read db
func ingestRows()
