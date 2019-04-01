package shvets

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import shvets.controlles.TextController
import shvets.services.TextService
import spock.lang.Specification

@SpringBootTest
class LoadContextTest extends Specification {
    @Autowired
    private TextController textController
    @Autowired
    private TextService textService

    def "check that all beans are created"() {
        expect:
        textService
        textController
    }
}
