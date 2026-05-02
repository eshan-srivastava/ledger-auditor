package lib

type Pool struct {
	workerCount int
}

type Worker struct {
}

func (w *Worker) Work(func() error) {

}
