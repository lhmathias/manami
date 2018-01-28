import io.github.manamiproject.manami.common.LoggerDelegate
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.slf4j.Logger
import kotlin.reflect.*


class LoggerDelegateSpec : Spek({

    describe("an instance of the logger") {
        val logger: Logger = LoggerDelegate().getValue(LoggerDelegateSpec::class.java, TestProp())

        it("must not be null") {
            assertThat(logger).isNotNull()
        }

        it("must be a logback instance") {
            assertThat(logger::class.java.toString()).isEqualToIgnoringCase("class ch.qos.logback.classic.Logger")
        }
    }
})

private class TestProp : KProperty<String> {
    override val annotations: List<Annotation>
        get() = mutableListOf()
    override val getter: KProperty.Getter<String>
        get() = TODO("not implemented")
    override val isAbstract: Boolean
        get() = false
    override val isConst: Boolean
        get() = true
    override val isFinal: Boolean
        get() = true
    override val isLateinit: Boolean
        get() = false
    override val isOpen: Boolean
        get() = false
    override val name: String
        get() = ""
    override val parameters: List<KParameter>
        get() = mutableListOf()
    override val returnType: KType
        get() = TODO("not implemented")
    override val typeParameters: List<KTypeParameter>
        get() = mutableListOf()
    override val visibility: KVisibility?
        get() = null

    override fun call(vararg args: Any?) = ""

    override fun callBy(args: Map<KParameter, Any?>) = ""
}