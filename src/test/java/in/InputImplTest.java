package in;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;


class InputImplTest {

    @Test
    void getInt_validInput_returnsInteger() {
        // Arrange
        String inputString = "5\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
        InputImpl input = new InputImpl();
        // Act
        int result = input.getInt(9);

        // Assert
        assertThat(result).isEqualTo(5);
    }

    @Test
    void getString_validInput_returnsString() {
        // Arrange
        String inputString = "Hello\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
        InputImpl input = new InputImpl();

        // Act
        String result = input.getString();

        // Assert
        assertThat(result).isEqualTo("Hello");
    }

    @Test
    void getDouble_validInput_returnsDouble() {
        // Arrange
        String inputString = "3.14\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
        InputImpl input = new InputImpl();

        // Act
        double result = input.getDouble();

        // Assert
        assertThat(result).isEqualTo(3.14);
    }

    @Test
    void getInteger_validInput_returnsInteger() {
        // Arrange
        String inputString = "12345\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
        InputImpl input = new InputImpl();

        // Act
        int result = input.getInteger(5, 1000, 99999);

        // Assert
        assertThat(result).isEqualTo(12345);
    }
}