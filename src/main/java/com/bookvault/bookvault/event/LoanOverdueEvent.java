package com.bookvault.bookvault.event;

import java.util.UUID;

public record LoanOverdueEvent(UUID loanId, String memberEmail) {
}
