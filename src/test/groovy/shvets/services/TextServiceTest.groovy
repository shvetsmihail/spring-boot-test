package shvets.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
class TextServiceTest extends Specification {
    @Autowired
    private TextService service

    @Unroll
    def "GetUniqueWordsCount"() {
        when:
        def result = service.getUniqueWordsCount(text)
        then:
        result == count
        where:
        text << [
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "Lorem ipsum dolor sit amet,consectetur adipiscing elit.",
                "Lorem ipsum dolor sit amet,consectetur adipiscing    elit.s.   ",
                "hello, my beautiful world!",
                "Hello it`s me"
        ]
        count << [8, 8, 9, 4, 3]
    }

    @Unroll
    def "GetUniquePunctuationCount"() {
        when:
        def result = service.getUniquePunctuationCount(text)
        then:
        result == count
        where:
        text << [
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "Lorem ipsum dolor sit amet,, consectetur adipiscing elit.",
                "Lorem ipsum dolor sit amet,, consectetur adipiscing elit.?!",
                ",.!?:;-",
                "Hello it`s me",
                "no punctuation"
        ]
        count << [2, 2, 4, 7, 0, 0]
    }

    def "GetReversedWords"() {
        when:
        def result = service.getReversedWords(text)
        then:
        result == reversed
        where:
        text << [
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "Hello it`s me"
        ]
        reversed << [
                "meroL muspi rolod tis tema, rutetcesnoc gnicsipida tile.",
                "olleH s`ti em"
        ]
    }
}
