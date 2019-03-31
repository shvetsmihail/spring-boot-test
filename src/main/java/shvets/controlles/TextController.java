package shvets.controlles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import shvets.models.TextCountResult;
import shvets.services.TextService;

@RestController
@RequestMapping(value = "/api/v.1.0/text/",
        consumes = MediaType.TEXT_PLAIN_VALUE)
public class TextController {
    private static final Logger LOG = LoggerFactory.getLogger(TextController.class);

    private TextService textService;

    @Autowired
    public TextController(TextService textService) {
        this.textService = textService;
    }

    @PostMapping(value = "/reversed", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> getReversedText(@RequestBody String text) {
        if (text == null || text.isEmpty()) {
            LOG.error("Status {}: empty body", HttpStatus.BAD_REQUEST);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Empty body");
        }

        try {
            String reversed = textService.getReversedWords(text);
            return new ResponseEntity<>(reversed, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Status {}: {}", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping(value = "/counts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextCountResult> getTextCountResult(@RequestBody String text) {
        TextCountResult textCountResult = new TextCountResult();
        long uniqueWordsCount = textService.getUniqueWordsCount(text);
        long uniquePunctuationCount = textService.getUniquePunctuationCount(text);
        textCountResult.setUniqueWordsCount(uniqueWordsCount);
        textCountResult.setUniquePunctuationCount(uniquePunctuationCount);
        return new ResponseEntity<>(textCountResult, HttpStatus.OK);
    }
}
