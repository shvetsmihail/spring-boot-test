package shvets.controlles

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import shvets.models.TextCountResult
import shvets.services.TextService
import spock.lang.Specification

import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@WebMvcTest
class TextControllerTest extends Specification {
    private static final BASE_PATH = "/api/v.1.0/text/"

    @Autowired
    private MockMvc mvc

    @MockBean
    private TextService service

    @Autowired
    private ObjectMapper objectMapper

    def "GetReversedText OK"() {
        given:
        when(service.getReversedWords("hello")).thenReturn("olleh")

        when:
        def response = mvc.perform(post(BASE_PATH + "reversed")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content("hello"))
                .andExpect(status().isOk())
                .andReturn().response

        then:
        response.status == 200
        MediaType.parseMediaType(response.contentType).equalsTypeAndSubtype(MediaType.TEXT_PLAIN)
        response.contentAsString == "olleh"
    }

    def "GetReversedText when body is empty"() {
        expect:
        mvc.perform(post(BASE_PATH + "reversed")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(""))
                .andExpect(status().isBadRequest())
    }

    def "GetReversedText when body is null"() {
        expect:
        mvc.perform(post(BASE_PATH + "reversed")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isBadRequest())
    }

    def "GetReversedText when TextService throw an exception"() {
        given:
        when(service.getReversedWords("hello")).thenThrow(new RuntimeException("oops"))

        expect:
        mvc.perform(post(BASE_PATH + "reversed")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content("hello"))
                .andExpect(status().isInternalServerError())
                .andReturn().response.errorMessage == "oops"
    }

    def "GetTextCountResult OK"() {
        given:
        def text = "hello world! It's me."
        when(service.getUniquePunctuationCount(text)).thenReturn(2L)
        when(service.getUniqueWordsCount(text)).thenReturn(4L)

        when:
        def response = mvc.perform(post(BASE_PATH + "counts")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(text))
                .andExpect(status().isOk())
                .andReturn().response

        then:
        response.status == 200
        MediaType.parseMediaType(response.contentType).equalsTypeAndSubtype(MediaType.APPLICATION_JSON)
        def jsonResult = response.contentAsString
        def result = objectMapper.readValue(jsonResult, TextCountResult)
        result.uniquePunctuationCount == 2
        result.uniqueWordsCount == 4
    }

    def "GetTextCountResult when body is empty"() {
        expect:
        mvc.perform(post(BASE_PATH + "counts")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(""))
                .andExpect(status().isBadRequest())
    }

    def "GetTextCountResult when body is null"() {
        expect:
        mvc.perform(post(BASE_PATH + "counts")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isBadRequest())
    }

    def "GetTextCountResult when TextService throw an exception"() {
        given:
        def text = "hello world! It's me."
        when(service.getUniquePunctuationCount(text)).thenReturn(2L)
        when(service.getUniqueWordsCount(text)).thenThrow(new RuntimeException("oops"))

        expect:
        mvc.perform(post(BASE_PATH + "counts")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(text))
                .andExpect(status().isInternalServerError())
                .andReturn().response.errorMessage == "oops"
    }
}
