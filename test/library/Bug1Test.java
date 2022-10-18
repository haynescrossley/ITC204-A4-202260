package library;

import library.entities.*;
import library.payfine.PayFineControl;
import library.payfine.PayFineUI;
import library.returnItem.ReturnItemControl;
import library.returnItem.ReturnItemUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class Bug1Test {
    
    Library library;
    Calendar calendar;
    Patron patron;
    Item book;
    Long bookId;
    ReturnItemControl returnItemControl;
    @Mock ReturnItemUI uiMock;
    PayFineControl payFineControl;
    @Mock PayFineUI payUIMock;
    Loan loan;
    
    @BeforeEach
    void setUp() throws Exception {
        library = Library.getInstance();
        patron = library.addPatron("j", "t", "jt@email", 123456789);
        book = library.addItem("author", "title", "1", ItemType.BOOK);
        bookId = 1L;
    
        calendar = Calendar.getInstance();
    
        loan = library.issueLoan(book, patron);
    
        calendar.incrementDate(4);
        library.updateCurrentLoanStatus();
    
        returnItemControl = new ReturnItemControl();
        returnItemControl.setUi(uiMock);
    
        returnItemControl.itemScanned(bookId);
        returnItemControl.dischargeLoan(false);
    
        payFineControl = new PayFineControl();
        payFineControl.setUi(payUIMock);
    }
    
    @Test
    void testFineAmount() {
        //arrange
        double fine = loan.getFines();
        //act
        //assert
        assertEquals(fine, patron.finesOwed());
    }
}
