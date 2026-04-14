package com.bookvault.bookvault.listener;

import com.bookvault.bookvault.event.LoanOverdueEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoanOverdueEventListener {

    @Async
    @EventListener
    public void handleLoanOverdue(LoanOverdueEvent event) {
        log.info("Notification sent for overdue loan: {}, member: {}", event.loanId(), event.memberEmail());
    }
}
