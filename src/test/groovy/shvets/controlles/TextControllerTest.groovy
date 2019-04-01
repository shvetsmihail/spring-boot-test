package shvets.controlles

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import shvets.models.ErrorResponse
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
        def text = "Hello"
        when(service.isEnglish(text)).thenReturn(true)
        when(service.getReversedWords(text)).thenReturn("olleH")

        when:
        def response = mvc.perform(post(BASE_PATH + "reversed")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(text))
                .andExpect(status().isOk())
                .andReturn().response

        then:
        response.status == 200
        MediaType.parseMediaType(response.contentType).equalsTypeAndSubtype(MediaType.TEXT_PLAIN)
        response.contentAsString == "olleH"
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
        def text = "Hello"
        when(service.isEnglish(text)).thenReturn(true)
        when(service.getReversedWords(text)).thenThrow(new RuntimeException("oops"))

        when:
        def response = mvc.perform(post(BASE_PATH + "reversed")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(text))
                .andExpect(status().isInternalServerError())
                .andReturn().response

        then:
        response.status == 500
        MediaType.parseMediaType(response.contentType).equalsTypeAndSubtype(MediaType.APPLICATION_JSON)
        def jsonResult = response.contentAsString
        def result = objectMapper.readValue(jsonResult, ErrorResponse)
        result.statusCode == 500
        result.errorMessage == "oops"
    }

    def "GetReversedText for non english text"() {
        given:
        def text = "Привет"
        when(service.isEnglish(text)).thenReturn(false)

        when:
        def response = mvc.perform(post(BASE_PATH + "reversed")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(text))
                .andExpect(status().isBadRequest())
                .andReturn().response

        then:
        response.status == 400
        MediaType.parseMediaType(response.contentType).equalsTypeAndSubtype(MediaType.APPLICATION_JSON)
        def jsonResult = response.contentAsString
        def result = objectMapper.readValue(jsonResult, ErrorResponse)
        result.statusCode == 400
        result.errorMessage == "It`s not an english text"
    }

    def "GetTextCountResult OK"() {
        given:
        def text = "hello world! It's me."
        when(service.isEnglish(text)).thenReturn(true)
        when(service.getAllPunctuationCount(text)).thenReturn(2L)
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
        result.allPunctuationCount == 2
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
        when(service.isEnglish(text)).thenReturn(true)
        when(service.getAllPunctuationCount(text)).thenReturn(2L)
        when(service.getUniqueWordsCount(text)).thenThrow(new RuntimeException("oops"))

        when:
        def response = mvc.perform(post(BASE_PATH + "counts")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(text))
                .andExpect(status().isInternalServerError())
                .andReturn().response

        then:
        response.status == 500
        MediaType.parseMediaType(response.contentType).equalsTypeAndSubtype(MediaType.APPLICATION_JSON)
        def jsonResult = response.contentAsString
        def result = objectMapper.readValue(jsonResult, ErrorResponse)
        result.statusCode == 500
        result.errorMessage == "oops"
    }
}
