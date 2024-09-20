package edu.eci.cvds.tdd.library;

import edu.eci.cvds.tdd.Library;
import edu.eci.cvds.tdd.library.book.Book;
import edu.eci.cvds.tdd.library.loan.Loan;
import edu.eci.cvds.tdd.library.user.User;
import edu.eci.cvds.tdd.library.loan.LoanStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {
    private Library library;
    private User user;
    private Book book;
    private Loan loan;

    @BeforeEach
    void setUp() {
        library = new Library();
        user = new User("John Doe", "123");
        book = new Book("Effective Java", "Joshua Bloch", "9780134685991");
        library.addUser(user);
        library.addBook(book);
        loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDateTime.now());
        loan.setStatus(LoanStatus.ACTIVE);
        library.loanABook(user.getId(), book.getIsbn());
    }

    @Test
    void testLoanABookUserNotFound() {
        Loan loan = library.loanABook("999", "9780134685991");
        assertNull(loan);
    }

    @Test
    void testLoanABookBookNotFound() {
        Loan loan = library.loanABook("123", "999");
        assertNull(loan);
    }

    @Test
    void testLoanABookBookNotAvailable() {
        library.loanABook("123", "9780134685991");
        Loan loan = library.loanABook("123", "9780134685991");
        assertNull(loan);
    }

    @Test
    void testLoanABookUserHasActiveLoan() {
        library.loanABook("123", "9780134685991");
        Loan loan = library.loanABook("123", "9780134685991");
        assertNull(loan);
    }

    @Test
    void testReturnLoanSuccess() {
        Loan returnedLoan = library.returnLoan(loan);
        assertNotNull(returnedLoan, "El préstamo debería devolverse correctamente.");
        assertEquals(LoanStatus.RETURNED, returnedLoan.getStatus(), "El estado del préstamo debería ser RETURNED.");
        assertNotNull(returnedLoan.getReturnDate(), "La fecha de devolución debería ser establecida.");
    }

    @Test
    void testReturnLoanAlreadyReturned() {
        library.returnLoan(loan);
        Loan secondReturn = library.returnLoan(loan);
        assertNull(secondReturn, "No debería ser posible devolver un préstamo que ya fue devuelto.");
    }
}
