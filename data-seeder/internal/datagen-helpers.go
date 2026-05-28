package internal

import (
	"fmt"
	"math"
	"strings"
	"time"
)

// resolveAmount computes the concrete integer amount for a rule.
func (d *DataGenerator) resolveAmount(a *RuleAmount, balances map[string]int64) (int64, error) {
	switch {
	case a.Fixed != nil:
		return *a.Fixed, nil
	case a.Range != nil:
		return d.randInt64(a.Range.Min, a.Range.Max), nil
	case a.SettleAccount != nil:
		bal := balances[*a.SettleAccount]
		if bal < 0 {
			return -bal, nil
		}
		return 0, nil
	case a.Pct != nil:
		// pct_of_source amounts are resolved at reactive-fire time, not here.
		return 0, fmt.Errorf("pct_of_source should be resolved reactively, not via resolveAmount")
	default:
		return 0, fmt.Errorf("rule has no valid amount spec")
	}
}

// applyBalance updates the account balances map after a transaction fires.
func (d *DataGenerator) applyBalance(balances map[string]int64, rule *RuleEntry, amount int64) {
	if rule.ToAccount != "" {
		// Internal transfer
		balances[rule.FromAccount] -= amount
		balances[rule.ToAccount] += amount
	} else {
		// External transaction
		accID := rule.Account
		if accID == "" {
			accID = rule.FromAccount
		}
		if rule.Direction == Debited {
			balances[accID] -= amount
		} else {
			balances[accID] += amount
		}
	}
}

// sampleFrequency draws a sample from a rounded normal distribution clamped
// to [0, mean+3*variance].
func (d *DataGenerator) sampleFrequency(mean, variance int) int {
	if variance == 0 {
		return mean
	}
	v := d.rng.NormFloat64()*float64(variance) + float64(mean)
	if v < 0 {
		return 0
	}
	return int(math.Round(v))
}

// randInt returns a seeded uniform random integer in [min, max].
func (d *DataGenerator) randInt(min, max int) int {
	if min >= max {
		return min
	}
	return min + d.rng.Intn(max-min+1)
}

// randInt64 returns a seeded uniform random int64 in [min, max].
func (d *DataGenerator) randInt64(min, max int64) int64 {
	if min >= max {
		return min
	}
	return min + d.rng.Int63n(max-min+1)
}

// returns string format of a 12-digit number
func (d *DataGenerator) accountNumberGenerator() string {
	n := d.randInt64(1e11, 1e12-1)
	return fmt.Sprintf("%d", n)
}

// mapAccountType converts a config type string to the AccountType enum.
func mapAccountType(t string) AccountType {
	switch strings.ToLower(t) {
	case "savings":
		return Savings
	case "credit":
		return Credit
	case "investment":
		return Investment
	case "trading":
		return Trading
	case "checking", "current":
		return Checking
	default:
		return Checking
	}
}

// parseWeekday converts a weekday name to time.Weekday.
func parseWeekday(s string) time.Weekday {
	switch strings.ToLower(s) {
	case "monday":
		return time.Monday
	case "tuesday":
		return time.Tuesday
	case "wednesday":
		return time.Wednesday
	case "thursday":
		return time.Thursday
	case "friday":
		return time.Friday
	case "saturday":
		return time.Saturday
	default:
		return time.Sunday
	}
}

// firstWeekdayOnOrAfter returns the first occurrence of wd on or after t.
func firstWeekdayOnOrAfter(t time.Time, wd time.Weekday) time.Time {
	diff := int(wd) - int(t.Weekday())
	if diff < 0 {
		diff += 7
	}
	return t.AddDate(0, 0, diff)
}

// daysInMonth returns the number of days in the month that t falls in.
func daysInMonth(t time.Time) int {
	//caling time.Date with day == 0 leads it to go for day before first day of the month
	//so doing time.Date with month+1 leads us to next month and then doing day=0 leads us to one day before
	return time.Date(t.Year(), t.Month()+1, 0, 0, 0, 0, 0, time.UTC).Day()
}

// firingEntry holds a scheduled transaction before it is posted.
type firingEntry struct {
	date       time.Time
	ruleIdx    int
	amount     int64 // pre-resolved; 0 means resolve at fire time
	triggerAmt int64 // for reactive_pct: the triggering transaction amount
}

// sortFirings is a stable insertion sort by date. Preserving declaration order
// for same-day ties is required so that reactive rules see the correct balance.
func sortFirings(firings []firingEntry) {
	for i := 1; i < len(firings); i++ {
		for j := i; j > 0 && firings[j].date.Before(firings[j-1].date); j-- {
			firings[j], firings[j-1] = firings[j-1], firings[j]
		}
	}
}
