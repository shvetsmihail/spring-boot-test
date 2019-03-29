package shvets.services

import spock.lang.Specification

class ServiceCharsTest extends Specification {
    def service = new ServiceChars()

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
        ]
        count << [8, 8, 9]
    }

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
        ]
        count << [2, 2, 4]
    }

    def "GetReversedWords"() {
        when:
        def result = service.getReversedWords(text)
        then:
        result == reversed
        where:
        text << ["Lorem ipsum dolor sit amet, consectetur adipiscing elit."]
        reversed << ["meroL muspi rolod tis tema, rutetcesnoc gnicsipida tile."]
    }
}
