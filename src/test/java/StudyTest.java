import domain.Study;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

/**
 * underscore를 빈 문자로 치환
 */
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 클래스마다 instance 생성, 클래스의 변수 공유
//@ExtendWith(FindSlowTestExtension.class) // 선언적인 등록
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 순서 정하기
class StudyTest {

    /**
     * 테스트 인스턴스마다 변수를 새로 만듬
     * 
     */
    int value = 1;

    @RegisterExtension // 프로그래밍 등록 방식
    static FindSlowTestExtension findSlowTestExtension =
            new FindSlowTestExtension(1000L);

    @Order(2)
    @DisplayName("스터디 만들기 fast")
    @FastTest // 커스텀 어노테이션 사용
    void create_new_study(){
        Study actual = new Study(value++);
        System.out.println(actual.getLimit());
        assertThat(actual.getLimit()).isGreaterThan(0);
    }

    @Order(1) // 동일한 숫자의 순서일 경우 우선순위 같음
    @DisplayName("스터디 만들기 slow")
    @Test // 커스텀 어노테이션 사용
    void create_new_study_again() throws InterruptedException {
        Thread.sleep(1005L);
        System.out.println("create1 " + value++);
    }

    @Order(3)
    @DisplayName("스터디 만들기")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
    void repeatTest(RepetitionInfo repetitionInfo){
        System.out.println("test " + repetitionInfo.getCurrentRepetition() + "/" +
                repetitionInfo.getTotalRepetitions());
    }

    /**
     * 파라미터의 개수만큼 매번 실행
     * @param message
     */
    @Order(4)
    @DisplayName("스터디 만들기1")
    @ParameterizedTest(name = "{index} {displayName} message={0}") // 0번째 파라미터
//    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요."})
//    @NullAndEmptySource// 비어 있는 문자열과 null 문자열
    @ValueSource(ints = {10, 20, 40})
    @CsvSource({"10, '자바 스터디'", "20, 스프링"})
    void parameterizedTest(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println(study.getLimit());
    }

    @DisplayName("스터디 만들기2")
    @ParameterizedTest(name = "{index} {displayName} message={0}") // 0번째 파라미터
    @CsvSource({"10, '자바 스터디'", "20, 스프링"})
    void parameterizedTest2(@AggregateWith(StudyAggregator.class) Study study) {
        System.out.println(study);
    }

    /**
     * inner static 또는 public 클래스여야 함
     */
    static class StudyAggregator implements ArgumentsAggregator {

        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return new Study(accessor.getInteger(0), accessor.getString(1));
        }
    }


    /**
     * 하나의 argument를 바꿀 때만 적용
     */
    static class StudyConverter extends SimpleArgumentConverter {

        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            Assertions.assertEquals(Study.class, targetType, "can only convert to domain.Study");
            Study study = new Study();
            study.setLimit(Integer.parseInt(source.toString()));
            return study;
        }
    }

    /**
     * 모든 클래스를 호출하기 전 단 한번 실행
     * 이 경우 static이어야 함
     */
    @BeforeAll
    static void beforeAll(){
        System.out.println("before all");
    }

    /**
     * 모든 클래스가 호출된 후에 단 한번 실행
     * 이 경우 static이어야 함
     */
    @AfterAll
    static void afterAll(){
        System.out.println("after all");
    }

    /**
     * 매번 테스트를 실행할 때마다 이전에 실행
     */
    @BeforeEach
    void beforeEach(){
        System.out.println("before each");
    }

    /**
     * 매번 테스트를 실행할 때마다 이후에 실행
     */
    @AfterEach
    void afterEach(){
        System.out.println("after each");
    }

}