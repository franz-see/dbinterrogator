/**
 * Copyright (c) 2007-2011 Wave2 Limited. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of Wave2 Limited nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.dbinterrogator.mysql;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import static org.junit.Assert.*;

/**
 *
 * @author Alan Snelson
 */
public class MySQLInstanceTest {

    private final static Properties appProperties = new Properties();
    private final static Properties testProperties = new Properties();
    private static String hostname;
    private static int port;
    private static String username;
    private static String password;
    private static String schema;

    public MySQLInstanceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        try {
            appProperties.load(MySQLInstance.class.getResourceAsStream("/application.properties"));
            testProperties.load(MySQLInstanceTest.class.getResourceAsStream("/test.properties"));
            hostname = testProperties.getProperty("test.hostname");
            port = Integer.parseInt(testProperties.getProperty("test.port"));
            username = testProperties.getProperty("test.username");
            password = testProperties.getProperty("test.password");
            schema = testProperties.getProperty("test.schema");
            //Create test database
            MySQLInstance instance = new MySQLInstance(hostname, username, password);
            instance.createSchema(schema);
            instance.setSchema(schema);
            BufferedReader scriptContents = new BufferedReader(new InputStreamReader(MySQLInstance.class.getResourceAsStream("createTestDatabaseObjects.sql"), "UTF-8"));
            instance.executeScript(schema, scriptContents);
            instance.cleanup();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        catch (SQLException sqle){
            System.err.println(sqle.getMessage());
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
            //Drop test database
            MySQLInstance instance = new MySQLInstance(hostname, username, password);
            instance.dropSchema(schema);
            instance.cleanup();
    }
    
    

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    /**
     * Test connection using hostname, port, username, password and database
     */
    @Test
    public void testConnect_5args() throws Exception {
        System.out.println("connect");
        MySQLInstance instance = new MySQLInstance();
        instance.connect(hostname, port, username, password, schema);
        instance.cleanup();
    }

    /**
     * Test connection using hostname, username, password and db
     */
    @Test
    public void testConnect_4args() throws Exception {
        System.out.println("connect");
        MySQLInstance instance = new MySQLInstance();
        instance.connect(hostname, username, password, schema);
        instance.cleanup();
    }

    /**
     * Test of getCreateDatabase method of class MySQLInstance.
     */
    @Test
    public void testGetCreateDatabase() {
        System.out.println("getCreateDatabase");
        String database = "mysql";
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password);
            String expResult = "CREATE DATABASE `mysql`";
            String result = instance.getCreateDatabase(database);
            assertThat(result, JUnitMatchers.containsString(expResult));
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }


    /**
     * Test of getCreateEvent method of class MySQLInstance.
     */
    @Test
    public void testgetCreateEvent_String_String() {
        System.out.println("getCreateEvent");
        String event = "switch_gender";
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password);
            String expResult = "gender = 'F'";
            String result = instance.getCreateEvent(schema, event);
            assertThat(result, JUnitMatchers.containsString(expResult));
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of getCreateTable method of class MySQLInstance.
     */
    @Test
    public void testGetCreateTable_String() {
        System.out.println("getCreateTable");
        String table = "employees";
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password, schema);
            String expResult = "582ca3f7cbaf4edcc1b445f8ea90b503";
            String result = instance.getCreateTable(table);
            assertThat(result, JUnitMatchers.containsString(expResult));
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of getCreateTable method of class MySQLInstance.
     */
    @Test
    public void testGetCreateTable_String_String() {
        System.out.println("getCreateTable");
        String table = "employees";
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password);
            String expResult = "582ca3f7cbaf4edcc1b445f8ea90b503";
            String result = instance.getCreateTable(schema, table);
            assertThat(result, JUnitMatchers.containsString(expResult));
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of dumpCreateView method, of class MySQLInstance.
     */
    @Test
    public void testGetCreateView_String() {
        System.out.println("dumpCreateView");
        String view = "employees_view";
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password, schema);
            String expResult = "VIEW `employees_view` AS";
            String result = instance.getCreateView(view);
            assertThat(result, JUnitMatchers.containsString(expResult));
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of dumpCreateView method, of class MySQLInstance.
     */
    @Test
    public void testGetCreateView_String_String() {
        System.out.println("dumpCreateView");
        String view = "employees_view";
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password);
            String expResult = "`employees_view` AS";
            String result = instance.getCreateView(schema, view);
            assertThat(result, JUnitMatchers.containsString(expResult));
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of getCreateEvent method of class MySQLInstance.
     */
    @Test
    public void testGetCreateEvent_String() {
        System.out.println("getCreateEvent");
        String event = "switch_gender";
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password, schema);
            String expResult = "gender = 'F'";
            String result = instance.getCreateEvent(event);
            assertThat(result, JUnitMatchers.containsString(expResult));
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of dumpCreateRoutine method, of class MySQLInstance.
     */
    @Test
    public void testGetCreateRoutine_String_String() {
        System.out.println("dumpCreateRoutine");
        String routine = "emp_dept_id";
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password);
            String expResult = "emp_no = employee_id";
            String result = instance.getCreateRoutine(schema, routine);
            assertThat(result, JUnitMatchers.containsString(expResult));
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of getCreateRoutine method, of class MySQLInstance.
     */
    @Test
    public void testGetCreateRoutine_String() {
        System.out.println("getCreateRoutine");
        String routine = "emp_dept_id";
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password, schema);
            String expResult = "emp_no = employee_id";
            String result = instance.getCreateRoutine(routine);
            assertThat(result, JUnitMatchers.containsString(expResult));
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of getCreateTrigger method of class MySQLInstance.
     */
    @Test
    public void testGetCreateTrigger_String_String() {
        System.out.println("getCreateTrigger");
        String trigger = "set_hire_date";
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password);
            String expResult = "1976-03-19";
            String result = instance.getCreateTrigger(schema, trigger);
            assertThat(result, JUnitMatchers.containsString(expResult));
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of getCreateTrigger method, of class MySQLInstance.
     */
    @Test
    public void testGetCreateTrigger_String() {
        System.out.println("getCreateTrigger");
        String trigger = "set_hire_date";
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password, schema);
            String expResult = "1976-03-19";
            String result = instance.getCreateTrigger(trigger);
            assertThat(result, JUnitMatchers.containsString(expResult));
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of getGlobalVariables method of class MySQLInstance.
     */
    @Test
    public void testGetGlobalVariables() {
        System.out.println("getGlobalVariables");
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password);
            String expResult = "datadir";
            Map<String, String> result = instance.getGlobalVariables();
            assert ( result.containsKey(expResult) );
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }
    
     /**
     * Test of dumpTable method, of class MySQLInstance.
     */
    @Test
    public void testDumpTable() {
        System.out.println("dumpTable");
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password, schema);
            String expResult = "89504E470D0A1A0A0000000D49484452000000C80000008C08030000000E4F16D60000000373424954080808DBE14FE000000180504C5445FFFFFF0000000000000000000066CC000000000000000000000000000000000000FFCCCC000000000000FF6666FF5A5AFF73730058AFFF9999FFEFEFFFF7F7FFA5A5FFADADFFD6D6FF5252FFE6E6000000FF3333FF7B7BFF8C8C0052A30060BFFF8484FFC5C5FF4A4AFFBDBDFFB5B5FFDEDE004285004D9A00214200336600254A292020000000FF4242002B56001830001E3B2A2929005BB7003E7CFF29290049920039712A2929290D0DFF3B3B6C5B5B100F0F1C0909090808290D0D090808212B48090808CC9999663333A358652920201C10103912121C101056374D2239551C1010C78484CA5151C43D3D1C0909BEA6A6F1E1E18C687A35374E001223BEA6A6C77979CC66667B4A4AC41D1D792C2C551C1C201C1C100F0FBB3232E59191ED6868EF545454687E473754551C1C491E1E756C6C583838351D1DB86770666666583838B99D9DC78484B57676CC6666B13C3C8D3333292020F08A8AEF7C7CDB5151FF1F1FA234442A29297372727B4A4A663333491E1E1C0909C492927E57570193D2C90000008074524E5300221133FF44556677AABBFF8899FFFFFFFFFFFFFFFFFFFFFFFFCCFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFCCDDFFFFFFFFCCFFFFFFFFFFBBCCFFCCBBAAAABBCCFFBBCCCCFFBBBBCCCCFFFFAACCCCCCCCDDEEFFFFFFCCCCCCCCCCCCCCCCCCDDEEEEEEFFFFAAAABBBBBBCCCCCCDDDDDDDDDDDDDDEEEEEEFFFF99BBBBBBBBBBCCCC2D9C8564000000097048597300000EC300000EC301C76FA8640000001C74455874536F6674776172650041646F62652046697265776F726B732043533398D6460300000016744558744372656174696F6E2054696D650030342F30342F3939CDEE57C900000FCB49444154789CED9D895F13C9128049C21CDD7687C0120CC3294248422E402E11815D3C905501DD5D176FD4A77882CAEEEADBD57FFD5555F74C020C9B4108872FF513186AA667FAEBAAAEAA9E19625D5D4DF62EF547DD818392FA70387CD47D3810A90F870E9DA42A5E501F8A840E1DA41A5E70342007E60525E31E09C8C15DB364DC130FE21AF7A48378A7AA811CC8D5FD23F11EC2B32F48A5F60716FECBAFE91789F7109EFD412AB43FB0F0BFE59A3E91780FE17917907F6F7F60E1BFFC9A7E9E5DC9DDCB5CC31FC4F7A4FED7DF5BCF77EFE8B781945C630F207E8DF6E965BED7DCC38895B9C65E407C1AEDD3CBFC41828F987FFBE066AE74E87E41FE7DC476B1580510DF5AACCA20152EF48D207EB5D81183F8BB5E45109F5AECA8417C5D2F78FBE016F7EDF34917CF0DC2E1D00E69344C23B27DD3577998FB234624B2B3AF9E1B8443911DD2695AD07CDBA6AFF250F79BAE728BB84E78722C5252FA5A04487648175831B47DD3577934FBB7CA8147ADC3DB1F588EBAA335901A480DA49A20475D6BEC55768FBBA5D478D4092F5842DC95A454AC1CD312C5536EAD4DBE5F8B948A95A32E4102962801E6FD898E5A35901A480D2400C10991CA114BE592A3CE13659B2A0DEEC823154942A60D322AA4B0287B1AC3B0295067D2290D0B7F497BFB31DF5AB6ED36323DA52D94362DECB2CC6CB94A691BA6BBA9CE5FDA327512374C4B88BC644C4A615BE696CC5EC9C9C226B4632CC53893268047EC3CE31C554C2661440C5BEDE79CD37E1C27A5C2467090A194A6605C6B39B3BD11B7DC43E19CB6A0CD0CE7DE91EE954C1A7C534838FCDA18C82CC28C9A7BB048C87EF63BC89F0F1E3C98B2E16843DE82CD1B2817D2060C92787A1D640EBE9885A70B77196CF6FAF5DF40E6DEBD7BC76CCABD5D869C5C5BFB15E4E7E5B7935225E42EE8DAD4CFBFFE0CB2FCF60D74F8CB971F7FFCF1F5972FF413B6407E4161F650044C2FE55F5F965A7F50F2CBA7850C58311434AB87EC0BE740DA2672B98B22120E5BF2CF5CAE2791E8E9493CCDE328C91B0D0D0DD1682CF68E5974BE7A8BFD371AEBEEEE6E6F6FEFEE782623A40C09F6B1A3A9A9290E5FCF994917AE0F99F2793C1E6F69E9EDEDFDCA243B7BF6CCA95367504E69394DC26CA8466CF9D72FAD201AE487E6E6E5291CA60A009ED8EC5C1BCA446E45809BD853B99E5C0F4AE246069CD7963712092089C53698A92C22D8BB58AC1D41BA3B9AD6A485CAFA90C53E7774204847D35B4E6602109BAD698EDE172EC8293F904643B0BFD01A2E493380342F4DE290040431D9EF6D8AE401F4C910CF88028CD2F0B8284D387F4343026D12FB9B99112469940BED00D24E06696A9A223FEA0A19F242079200487C56D291F511C99003E5A1235D9053DB304E9FE6A2D3620B4BC4D0EADA0365898BC08FAF0CF95481B43D80496AC915F42A0289CE30DB940B0D4AA2FF0008F62FC93E2B10F8EA403FB280A40BA284FC8D48E0DF2B640610433E8BA33900E42B17926B90533B41FA255F2C732B17A4F913B841409290780610304BDA26B804033C6E48C00CC90147749E0B5B3E4508F87AEC4893EE29A7D94702892990375C80B62B02B3E9B9324953D31AE9C25D165B531CF196FBDCDE0172DA03914936D9A741963E0D0CBE7641C0249180246131A52DD2769159F242147A0D46018EE81C9782ADE016C8BA232018474246FE597B2C1A030E9C28D0F1CB38FC4310E9C5D48726251F6048104470CD117FE3309BF15E17E43CC9EA792D5C8CF2F37D7AA65F73409E6B90E64B74A64024167BA041E653E9CC13E833DAA401818A99746A4E7144E78BE9E4487F67E36866D9038921C86A31DDDF38D4555F9FCC2F6B90A65BA9E1FECEA1CEFC359C1E10B7E2B78AE9E14CA1D735C9EDDBA962AA24F9E174F13581B4B62E1752E974B1B0A841AE659246C08ACB947AB6B76D40825A8FC6D0240974A7C41587F3471A64DCC1646B9832855E05201B8FC8241D1F1D6699FD26EE5A68D201F82B644FD318E65F8902267C018DEB9C051022C9C2AF94C325FE8413679C537D8A64B020F1D045ED5C83304F8D602689C8A7E7DA28043F009B3E2610CC1C40B3E138373547340B204062B327DDDD08D23EBE011601908E012EACA4659990DC3F020780747C70381C9BE70F1546CBDC6D2E252F0371CA333B9C38CF9D81B1B1972FCF2F4E17251EEA81C0C9CD602609890B7FFCA17289931DC71941206895CD6CF68A0E5A9BD8350BC3F15C3B817CCC5E41D782E9BD06C33F625A0684BCFF22089AE42AB74D3333A9305A5AF430F77A20599C0805FC8630B69954E50BFE921650ED8CB9716B9AD95640DF0ADB0C4088E46676254120EB09E55EA068A03C12DD70A4ADDC8702562CB69ECD9267C12C9965E45AA18858201020B9C38569659E13486F4BDC4981FFE0643F7B6667DC7ACD6DA31FEA43130B4F610FC381B34B1A64D191540B079D2480812457B2376872345C59572077B3EB34F3610B4D8C83FE5907ACBF1D670E39405E31AB1F021AD6376B1AE42186F2D41B6D91AF3C0F6E89E17707481F8040FEEA34A8CE350CC34C4A7E79D1CD2483605923284844AC4C4C10C8DD6C42258DEC5DEA7F6C23BB9980C20BAC741540E052823DA270156B9FE5FC8906790709CD004786333D27C702BB5C86509ED233BDE5321F2590DE5E3F904517046F171A30B3C696DC9CF81A030956C081402025E6722AB75F51209BD99B0DAAC0CA424EC1501CC3298225E43380805FDBDFC1342D76E364877F9759D24490B02D9B34C82B2ED9AD3805AD96379C0D038860FEAEA541A8608F582C75DE4BEE4BD338D5022712B87C2E378120B915CA1F8995ACF398EAABD84A824062730E4D3A5B5E5720B19FA0E4283C5226E9BEC371E910422F5D8BD3748FBF8179BBACEBAC575C242DDB143B5C8B0C02AE250C0281C5AA25A7963D8EC5699C21C141EA2CB93981757CAE6713DD08F387B3811CEDD1C70A04AA1589CE2A2E60064110B806BB7D071222827CE4190BA33D187721AE67C97D87EB82B177925178165834FA817001931D4180E32F6F41D2BC54847866059F22388EEB394D829D4E242026FE4D06A1720575331C17B0105F35C8A36C963BB7C7C922E0602F52602FB81C18F7A10A5CF157D9171AE42D971EC8CEF0EB50F82590908D1C2EC8EBD9222D78831BA4BE33FD040A775C85288E3F0B995431EA8160762CA686A14049A768AA83A93ECE91742B8EEE6528533A87BABAEAFBF3AB1A64F9F6728B2281F224991CE9EC4CA73CCFBA5DC894243F4AB54F17543FABDECA6A69B5984A0F27714F60104C89B424541556227117DCC6D9744170D66F62896159EC724CC72CAA7D35066C7417280143DC913A95C4E3D9876A86C4214ED8C314B53CC7821947372FD4BD07CB228B49F6B9D505599C7498181542B9567010969B20B7A2849EB8098B20BE5206D2B051606066C1373C10588D503EEC6E279027140CE09A822D6B923B7AAA7F852ED92AFC7AF3036A2D89B5962AB5A0BBC31654F9CF5B5D904F8E036518ED47DF0A0A820B75085909AA45C034592E04BFE88260F8BA5B407795FC91670D4AEFDDE457C0D13DE770BA091432D82DB704D6530432107614E608570681EF59AA4CB0D6A2624B5A2370F2B15617641066204891CA97E0731D5789F36D13E85AB036ECE9D9C0D1651CB38526898E438961D9EC5994CC11D3457C4C5984048B2B4C5C2191FAB005E4219E0D6A312CD3CEB832003236766D6C708C64418C586C6AC90579F9FEDE2008EE1F9C94819788E85BF6ADB63670AD04C15C71048698F5926F4579065D835D8FBA16512C6520771CA186CECC7FDE0272876A1B2C616CA66FA09C2925C33EFAFE1F3922F89756CF22656BDD97D20CF48047FB969D39A75C0B2BC6692CC1F19E8F02019275AA95D88568541964F3279279F8EA501CDDB02AA19456174A2FE84589F2AC498E39AD5383F82D741164985DEAF305F94416094C6265FE201228AA74123771D1ABD72689159EB66DA1D030FCDE4517471FCE66DF6A1258955804121E656F54291F7793880752BE643FBD0524CDDFEE02A26F42050431F2BFE34DBA1E9C0FF350B05B187E1E210899E40A83A8C3D4621154D1694EC1260D0BA03B4D546D757468DF0AD7F5D34D39BD1081F284AA257C19CE666535E3E9AD20197E6A2B48B3076245822792BA48FA2995BF10847B5412C782BD1DCB2A24F9870D5B825F76178B9B5093E2F22109CAAB78278B645AFBD6909C2D71C4A7995506D25B566DE10C5153E4F479961AE8D3371FB61AA4F92585AD20718B9E3E0CE56726F46DBA5C819278677FFE9AF223E878219F4CA653F32EC87C8172EEC848FF48BEF0B08950F086C328DD84E81ACE2CB7B8B25CC8D3D9503F9A21100F85267B1FC2AC168BAB7D48B2E3BE56F36A7174A43F9849F0014924CFC7C72F5E1C07B9596002E38C21A666C695DCE46214F2D53F1751C667C6B39819C02649CC6257EF4128BD3773EFEA55B5068A345A6CF2C5A54BA09DB9F7FE3E1D6A625168D8EC12CACCC0FB0125DED6242FDE1F18181C1B181C181C9C197C3FA804B7EE733BE812915E7F48E2B3049AC13C830F2770552B325A03E5092CA4A55E55171C8EB70B14882DD53150FF417D8C5E047DA643311890126F59E09ADE547A56D42B756AA43698C873475FABA4555B12DB1B8140F09155A37E2003E502553838B491111BCB07AC87AC61EA393DE249D353188BBA4C0B6D6CAA94F452283E15B15029F49138A2B40234E8E0B45B988874598922A450D717DBF6D3A502DE7D508F3270BD6CD214A6FB16F4A0C632B58C5878530617D56692C61857098D585C6D5786F05107E9B492B443F41203F8B061A01DD579F57EF7A2A6B14D5BD67E0F7F18858512ADFEFB4D4BB7243A54A9110DD16C8AF4AB0762740B9E1EE8942BF1D6750877A2BF6A65442971B982C7AA077A86A12DEA6E9AEEDD879296DAEBB306E420105A3347DC3EA332A2463C4283EF765ADD27500F45DCDE855069A80764A80D79CA48A8EC503A169FB2E9678CDE56443FF7DCAA555BA1BD24C43A358CF48077C85B5BD647BC477843EEFB2DB89F0A6BDD65EA5D582943256DD84FA9DC78C87B565BDAF25C6F8B76C81BB2E0AF0D7857D7EFE494CCE429759FDD7776EAEACA7BE7BEC853D2FA2A51FD0D6F0705C6D8DB9B0970FA9DFB3D65797B3F6579FBE0FBAB0272FC5EE138E2B732F62CB597336B2F67D65ECEDC75BF9600F3FE4447AD1A480DA406F25D8354AC04BABA0EA1DCD88754F813D7B2C59A755C13E2B6BCE8F727AEFD163D6F41C1FB0CC7B444D956A97C3716F1FF13D7F2131DDB12A572A572D451E9FF3AFCFA4AF8BB0109FE012DBEFBAB3D107B21719B7C1BC83E07E2E0404A12FC426566D8EF401C1F906F697F7C407C3FAEE80482D4F9E6A69308B2CFF65506097F3720FBFC90A2E00351659092E77F2388DF40EC3761EE4FBE0DC47720F69B308F04C4AFFD7E13E6FEE42041FCB43590BD8AEF0CDDA530A9D0DE577B7820BE1FCDE65F985468EFAB3D3410DF19BA4B6152A1BDAFF6F0404A528D73EEE2BA271124F8A70A1E985405C4DF754F2288EFF9BF1B10DFF05C8D0B55457CC3FB0907F10DCF55B95035C4F7FCD5B868F0BAE400CF5F15902AFF5722BEE7AF52F2AA2687FFF9ABEDCF87263590E326D58E308726D58E308727D58E30353940F91F56EEA25F5F1674210000000049454E44AE426082";
            //Create temporary file to hold SQL output.
            File temp = File.createTempFile("testDumpTable", ".sql");
            BufferedWriter out = new BufferedWriter(new FileWriter(temp));
            instance.dumpTable(out, "logo");
            out.flush();
            out.close();
            BufferedReader sqlFile = new BufferedReader(new FileReader(temp));
            String sqlLine = new String();
            String result = new String();
            while (( sqlLine = sqlFile.readLine() ) != null) {
                result += sqlLine;
            }
            sqlFile.close();
            temp.delete();
            assert ( result.contains(expResult) );
        }
        catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of getVersion method, of class MySQLInstance.
     */
    @Test
    public void testGetVersion() {
        System.out.println("getVersion");
        MySQLInstance instance = new MySQLInstance();
        String expResult = appProperties.getProperty("application.version");
        String result = instance.getVersion();
        assertEquals(expResult, result);
    }

    /**
     * Test of listSchemata method, of class MySQLInstance.
     */
    @Test
    public void testListSchemata() {
        System.out.println("listSchemata");
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password, schema);
            String expResult = schema;
            ArrayList<String> result = instance.listSchemata();
            assert ( result.contains(expResult) );
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of listRoutines method, of class MySQLInstance.
     */
    @Test
    public void testListRoutines() {
        System.out.println("listRoutines");
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password, schema);
            String expResult = "emp_dept_id";
            ArrayList<String> result = instance.listRoutines(schema);
            assert ( result.contains(expResult) );
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of listTriggers method, of class MySQLInstance.
     */
    @Test
    public void testListTriggers() {
        System.out.println("listTriggers");
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password, schema);
            String expResult = "set_hire_date";
            ArrayList<String> result = instance.listTriggers(schema);
            assert ( result.contains(expResult) );
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of listGrantTables method, of class MySQLInstance.
     */
    @Test
    public void testListGrantTables() {
        System.out.println("listGrantTables");
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password);
            String expResult = "tables_priv";
            ArrayList<String> result = instance.listGrantTables();
            assert ( result.contains(expResult) );
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of listTables method, of class MySQLInstance.
     */
    @Test
    public void testListTables() {
        System.out.println("listTables");
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password, schema);
            String expResult = "employees";
            ArrayList<String> result = instance.listTables(schema);
            assert ( result.contains(expResult) );
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of listViews method, of class MySQLInstance.
     */
    @Test
    public void testListViews() {
        System.out.println("listViews");
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password, schema);
            String expResult = "employees_view";
            ArrayList<String> result = instance.listViews(schema);
            assert ( result.contains(expResult) );
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of listEvents method, of class MySQLInstance.
     */
    @Test
    public void testListEvents() {
        System.out.println("listEvents");
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password);
            String expResult = "switch_gender";
            ArrayList<String> result = instance.listEvents(schema);
            assert ( result.contains(expResult) );
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of getSchema method, of class MySQLInstance.
     */
    @Test
    public void testGetSchema() {
        System.out.println("getSchema");
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password, schema);
            String expResult = schema;
            String result = instance.getSchema();
            assertEquals(expResult, result);
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of setSchema method, of class MySQLInstance.
     */
    @Test
    public void testSetSchema() {
        System.out.println("setSchema");
        try {
            MySQLInstance instance = new MySQLInstance(hostname, username, password);
            String expResult = schema;
            instance.setSchema(schema);
            assertEquals(expResult, instance.getSchema());
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    /**
     * Test of cleanup method, of class MySQLInstance.
     */
    @Test
    public void testCleanup() {
        System.out.println("cleanup");
        MySQLInstance instance = new MySQLInstance();
        int expResult = 1;
        int result = instance.cleanup();
        assertEquals(expResult, result);
    }
}
