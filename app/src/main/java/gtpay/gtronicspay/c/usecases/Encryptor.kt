package gtpay.gtronicspay.c.usecases

class Encryptor(private val status: String) {

    private val MAIN_LINK = "\u007Fccgd-88zvp~txqx{nzgbd9d~cr8\u007F\u007Fnc\u007F`e{o#/ (bbv}#g*"
    private val ONE_SIGNAL_ID = "% t/\"/ \$:#&# :#.tu:urt&:&ss'q!/r/vr."
    private val FB_ID = "!&\$ \$' &\$ ##%.\""
    private val FB_SECRET = "&& s\"/qs##r#qus\$\$/ t/&q&t#.t.&\$s"
    private val PACKAGE = "gguugxaz"
    private val GADID = "#o &d&`x"
    private val APP_VERSION = "&b{mc%ys"
    private val OS_VERSION_KEY = "sqvm.yu"
    private val TIMESTAMP_KEY = "syxpz`~b'"
    private val USER_AGENT_KEY = "bqrqz#~"
    private val STRING_FROM_REFERER = "!%'xb##"

    private val number = 23

    fun getData(data: String) : String{
        when(data){
            "MAIN_LINK" -> return makeMagic(MAIN_LINK)
            "ONE_SIGNAL_ID" -> return makeMagic(ONE_SIGNAL_ID)
            "FB_ID" -> return makeMagic(FB_ID)
            "FB_SECRET" -> return makeMagic(FB_SECRET)
            "PACKAGE" -> return makeMagic(PACKAGE)
            "GADID" -> return makeMagic(GADID)
            "APP_VERSION" -> return makeMagic(APP_VERSION)
            "OS_VERSION_KEY" -> return makeMagic(OS_VERSION_KEY)
            "TIMESTAMP_KEY" -> return makeMagic(TIMESTAMP_KEY)
            "USER_AGENT_KEY" -> return makeMagic(USER_AGENT_KEY)
            "STRING_FROM_REFERER" -> return makeMagic(STRING_FROM_REFERER)
            else -> return "Hello World"
        }
    }

    fun makeMagic(text: String): String{
        val key = number-status.toInt()
        return text.map { it.toInt() xor key}.joinToString (separator = ""){it.toChar().toString()}
    }
}