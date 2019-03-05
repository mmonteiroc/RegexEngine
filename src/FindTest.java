import org.junit.Test;

import static org.junit.Assert.*;

public class FindTest {
    @Test
    public void simple_matches() {
        Find f = new Find("Liceu");
        assertEquals(true, f.match("ic"));
        assertEquals(false, f.match("li"));
        assertEquals(true, f.match("eu"));
        assertEquals(true, f.match("i"));
        assertEquals(false, f.match("ec"));
        assertEquals(false, f.match("xet"));
        assertEquals(false, f.match("Liceuu"));
        assertEquals(true, f.match("Liceu"));
        assertEquals(false, f.match(""));

        f = new Find("Esternocleidomastoideo");
        assertEquals(true, f.match("Ester"));
        assertEquals(true, f.match("E"));
        assertEquals(true, f.match("eido"));
        assertEquals(true, f.match("oid"));
        assertEquals(true, f.match("oideo"));
        assertEquals(true, f.match("cle"));
        assertEquals(false, f.match("ccl"));
        assertEquals(false, f.match("Liceu"));
        assertEquals(false, f.match("ester"));
        assertEquals(false, f.match("Eo"));
        assertEquals(false, f.match("Esternocleidomastoideoascds"));
        assertEquals(true, f.match("Esternocleidomastoideo"));
    }

    @Test
    public void specials() {
        Find f = new Find("a?b");
        assertEquals(true, f.match("a@?b"));

        f = new Find("bill.gates@microsoft.com");
        assertEquals(true, f.match("s@@micro"));

        f = new Find("ho[az]");
        assertEquals(true, f.match("o@["));
        assertEquals(true, f.match("z@]"));

        f = new Find("stars*d+");
        assertEquals(true, f.match("s@*"));
        assertEquals(true, f.match("@*d@+"));
        assertEquals(true, f.match("ars@*"));

        f = new Find("%hello$");
        assertEquals(true, f.match("@%"));
        assertEquals(true, f.match("@$"));

    }

    @Test
    public void qmark() {
        Find f = new Find("Llibre");
        assertEquals(true, f.match("Llibre"));
        assertEquals(true, f.match("Llibr?"));
        assertEquals(false, f.match("Llibp?"));
        assertEquals(true, f.match("?libr?"));
        assertEquals(true, f.match("L?ib"));
        assertEquals(false, f.match("i??ke"));
        assertEquals(false, f.match("ab?cd"));
        assertEquals(false, f.match("Ll?ibre"));
        assertEquals(true, f.match("?ib"));
        assertEquals(true, f.match("?"));
        assertEquals(false, f.match("???????"));
        assertEquals(true, f.match("??????"));
    }

    @Test
    public void positions() {
        Find f = new Find("This computer");
        assertEquals(true, f.match("%This"));
        assertEquals(false, f.match("%his"));
        assertEquals(false, f.match("%computer"));
        assertEquals(true, f.match("%This computer"));
        assertEquals(true, f.match("%This computer$"));
        assertEquals(true, f.match("This computer$"));
        assertEquals(true, f.match("This computer"));
        assertEquals(false, f.match("That computer"));
        assertEquals(false, f.match("%computer$"));
        assertEquals(false, f.match("%This computer$ is black$"));
        assertEquals(true, f.match("ter$"));
        assertEquals(false, f.match("This$"));

        f = new Find("This computer$ is black");
        assertEquals(true, f.match("%This computer$ is black$"));
        f = new Find("This computer$ is black$");
        assertEquals(false, f.match("%This computer$ is black$"));
        f = new Find("%This computer$ is black$");
        assertEquals(false, f.match("%This computer$ is black$$"));
        f = new Find("%This computer$ is black$");
        assertEquals(true, f.match("%%This computer$ is black$$"));
    }

    @Test
    public void charClasses() {
        Find f = new Find("This is your life");
        assertEquals(true, f.match("T[h]is"));
        assertEquals(true, f.match("T[abhc]is"));
        assertEquals(false, f.match("T[abc]is"));
        assertEquals(true, f.match("[tT]hi[ksn]"));
        assertEquals(false, f.match("This [is] your life"));
        assertEquals(true, f.match("This is [sdfyjkl]our life"));
    }

    @Test
    public void charClasses2() {
        Find f = new Find("Do what you can");
        assertEquals(true, f.match("wha[r-v]"));
        assertEquals(false, f.match("[a-m]hat"));
        assertEquals(true, f.match("D[h-z] wha[j-u]"));
        assertEquals(false, f.match("[a-z]o "));
        assertEquals(true, f.match("[A-Z]o "));
        assertEquals(false, f.match("ca[A-Z]"));
        assertEquals(true, f.match("ca[A-Zn]"));
        assertEquals(true, f.match("ca[A-Za-z]"));
    }

    @Test
    public void closures1() {
        Find f = new Find("bb");
        assertEquals(true, f.match("b+"));
        assertEquals(true, f.match("[abc]+"));
        assertEquals(false, f.match("b[ac]+"));

        f = new Find("aaaaaaabc");
        assertEquals(true, f.match("a+bc"));
        assertEquals(false, f.match("a+kbc"));
        assertEquals(true, f.match("ab+c"));
        assertEquals(false, f.match("abb+c"));
        assertEquals(false, f.match("az+c"));
        assertEquals(true, f.match("a+bc+$"));
        assertEquals(true, f.match("%[abc]+$"));
        assertEquals(false, f.match("%[ab]+$"));
        assertEquals(false, f.match("az+bc"));
    }

    @Test
    public void closures2() {
        Find f = new Find("bb");
        assertEquals(true, f.match("b*"));
        assertEquals(true, f.match("[abc]*"));
        assertEquals(true, f.match("b[ac]*"));

        f = new Find("aaaaaaabc");
        assertEquals(true, f.match("a*bc"));
        assertEquals(false, f.match("a*kbc"));
        assertEquals(true, f.match("ab*c"));
        assertEquals(true, f.match("abb*c"));
        assertEquals(false, f.match("abbb*c"));
        assertEquals(true, f.match("az*bc"));

        f = new Find("192228888888888888886722222226");
        assertEquals(true, f.match("192*8*672*6"));
        assertEquals(false, f.match("2*78*6"));
        assertEquals(true, f.match("1*"));
        assertEquals(true, f.match("8*"));
        assertEquals(true, f.match("k*"));
        assertEquals(true, f.match("%1[92867]*6$"));
        assertEquals(false, f.match("14*2"));
        assertEquals(true, f.match("14*9"));
        assertEquals(false, f.match("14*9$"));
    }

}