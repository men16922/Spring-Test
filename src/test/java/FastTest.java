import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 커스텀 어노테이션
 */
@Target(ElementType.METHOD) // 애노테이션을 메소드에 사용가능
@Retention(RetentionPolicy.RUNTIME) // 애노테이션 정보 runtime까지 유지
@Test
@Tag("fast") // fast 태그
public @interface FastTest { // test와 tag를 합친 커스텀 어노테이션 생성
}
