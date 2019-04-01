package shvets.controlles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shvets.models.TextCountResult;
import shvets.services.TextService;

@RestController
@RequestMapping(value = "/api/v.1.0/text/",
        consumes = MediaType.TEXT_PLAIN_VALUE)

public class TextController {
    private TextService textService;

    @Autowired
    public TextController(TextService textService) {
        this.textService = textService;
    }

    @PostMapping(value = "/reversed", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> getReversedText(@RequestBody String text) {
        validateRequest(text);
        String reversed = textService.getReversedWords(text);
        return new ResponseEntity<>(reversed, HttpStatus.OK);

    }

    @PostMapping(value = "/counts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextCountResult> getTextCountResult(@RequestBody String text) {
        validateRequest(text);
        TextCountResult textCountResult = new TextCountResult();
        long uniqueWordsCount = textService.getUniqueWordsCount(text);
        long uniquePunctuationCount = textService.getAllPunctuationCount(text);
        textCountResult.setUniqueWordsCount(uniqueWordsCount);
        textCountResult.setAllPunctuationCount(uniquePunctuationCount);
        return new ResponseEntity<>(textCountResult, HttpStatus.OK);
    }

    private void validateRequest(String text) {
        if (!textService.isEnglish(text)) {
            throw new IllegalArgumentException("It`s not an english text");
        }
    }
}