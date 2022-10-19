package library;

import library.borrowitem.BorrowItemControl;
import library.borrowitem.BorrowItemUI;
import library.entities.*;
import library.payfine.PayFineControl;
import library.payfine.PayFineUI;
import library.returnItem.ReturnItemControl;
import library.returnItem.ReturnItemUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class Bug2Test {
    Library library;
    Patron patron;
    Item book;
    Item dvd;
    Item vhs;
    int bookId;
    int dvdId;
    int vhsId;
    Long patronId;
    BorrowItemControl borrowItemControl;
    @Mock
    BorrowItemUI uiMock;
    Loan loan;
    @BeforeEach
    void setUp() throws Exception {
        library = Library.getInstance();
        patron = library.addPatron("j", "t", "jt@email", 123456789);
        book = library.addItem("author", "title", "1", ItemType.BOOK);
        dvd = library.addItem("author", "title", "1", ItemType.DVD);
        vhs = library.addItem("author", "title", "1", ItemType.VHS);
        
        patronId = 1l;
        bookId = 1;
        dvdId = 1;
        vhsId = 1;
        
        borrowItemControl = new BorrowItemControl();
        borrowItemControl.setUI(uiMock);
        borrowItemControl.cardSwiped(patronId);
    
        borrowItemControl.itemScanned(bookId);
        borrowItemControl.itemScanned(dvdId);
    
        loan = library.issueLoan(book, patron);
        loan = library.issueLoan(dvd, patron);
    }
    
    // The patron has 2 loans already, attempt to scan another item. The patron should not be able to
    // as they have reached the loan limit.
    @Test
    void test1() {
        //arrange
        //act
        Executable act = () -> borrowItemControl.itemScanned(vhsId);
        //assert
        assertThrows(RuntimeException.class, act);
        assertEquals(library.getLoanLimit(), patron.getNumberOfCurrentLoans());
    }
    
}