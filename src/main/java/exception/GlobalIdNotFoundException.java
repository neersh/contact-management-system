package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalIdNotFoundException {
    @ExceptionHandler(IdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody

    public ErrorMessage handleNoRecordFoundException(IdNotFoundException ex) {

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage("ID CAN'T INVALID");
        return errorMessage;
    }
}
