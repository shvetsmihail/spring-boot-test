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
                "Hello it`s me",
                "\tfirst line,\n\tsecond line",
        ]
        count << [8, 8, 9, 4, 3, 3]
    }

    @Unroll
    def GetAllPunctuationCount() {
        when:
        def result = service.getAllPunctuationCount(text)
        then:
        result == count
        where:
        text << [
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "Lorem ipsum dolor sit amet,, consectetur adipiscing elit.",
                "Lorem ipsum dolor sit amet,, consectetur adipiscing elit.?!",
                ",.!?:;-",
                "Hello it`s me",
                "no punctuation",
                "\tfirst line,\n\tsecond line",
        ]
        count << [2, 3, 5, 7, 0, 0, 1]
    }

    def "GetReversedWords"() {
        when:
        def result = service.getReversedWords(text)
        then:
        result == reversed
        where:
        text << [
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "Hello it`s me",
                ". Hello, World",
                "\tfirst line,\n\tsecond line",
                " first line, \n second line",
        ]
        reversed << [
                "meroL muspi rolod tis tema, rutetcesnoc gnicsipida tile.",
                "olleH s`ti em",
                ". olleH, dlroW",
                "\ttsrif enil,\n\tdnoces enil",
                " tsrif enil, \n dnoces enil",
        ]
    }

    @Unroll
    def "IsEnglish"() {
        expect:
        service.isEnglish(text) == result

        where:
        text << [
                "\tHello World.\n It`s me, 27",
                "\tПривет Мир.\n It`s me, 27"
        ]
        result << [true, false]
    }
}
