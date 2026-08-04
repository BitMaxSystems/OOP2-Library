package org.bitmaxsystems.oop2library.util.bookformchain;

import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;
import org.bitmaxsystems.oop2library.util.contracts.IBookFormChain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyBookDataChain implements IBookFormChain {
    private IBookFormChain nextChain;
    private List<String> errorList = new ArrayList<>();

    @Override
    public void setNextChain(IBookFormChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(BookDataDTO bookDataDTO) throws Exception {
        Pattern pattern = Pattern.compile("\\d{3}-\\d-\\d{2}-\\d{6}-\\d");
        Matcher matcher = pattern.matcher(bookDataDTO.getIsbn());

        if (!matcher.find())
        {
            errorList.add("- ISBN is not in the correct format.");
        }

        if (bookDataDTO.getTitle().isBlank())
        {
            errorList.add("- Title cannot be empty.");
        }

        if (bookDataDTO.getGenre() == null)
        {
            errorList.add("- Genre not selected.");
        }

        if (bookDataDTO.getAuthor() == null)
        {
            errorList.add("- Author not selected.");
        }

        if (bookDataDTO.getPublisher() == null)
        {
            errorList.add("- Publisher not selected.");
        }

        if (errorList.isEmpty())
        {
            if (nextChain != null)
            {
                nextChain.execute(bookDataDTO);
            }
        }
        else
        {
            StringBuilder sb = new StringBuilder();

            for (String error: errorList)
            {
                sb.append(error).append("\n");
            }

            throw new DataValidationException(sb.toString());
        }
    }
}
