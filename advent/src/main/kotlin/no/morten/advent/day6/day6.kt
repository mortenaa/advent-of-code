package no.morten.advent.day6

fun findFirstMarker(message: String, length: Int) =
    message.windowed(length).indexOfFirst { it.toSet().size == length } + length

fun main() {
    val s = "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"
    println(findFirstMarker(message, 4))
    println(findFirstMarker(message, 14))
}

val message = "mgwwjddqzqdqsstctjjsdjsdsrsfsmfsfwwltwlwhwnhhlffzddgffwlffbsfshfshhgvvdrrltlzlnzznrrnrsnnhgnnfjnnvpnnbjjnwwrcwrrhlhvlhhmzmqzqrqtqmqpmpwwmssgsrgrgtgmtgmtgtdtvdvmvsvsbvsbvbtthmmftmmdnmddcrcvcrrfjfhhfjhffjllcpllmcctjtrttwmtwmwffrlrqlqzzpddsqdqqgjqgjgngwnncjnnvsnswwbzbtzzflzzqsqbsbvbmbnnjpnpnnpfpmpmnpmmjljtltssqnsqslstswtwswwjddvmmzlzqlzqzqjjlttmtrtbtmtgmtmsttrctrrsqrqvvrzrcrhhlnhllbfbtthrhdhllmwlmlgglgsgmgsmszzprpwpfprfftffpssjzjgzjzddqfqmmwqwvwlvlqqtbtwwrwttmsmppbmmpcmctcnnhssnjncnlcnctcjjrzrwrfwfcwffczztrtsrtstlsssljssmvssjzssrqqrcqqwlqwlwffsflssrrzhzzhrzzdgdppspwplpqptttvddggzszccrrnzzwwdwjddrvvwggpvgpvvhdhqddffrnngcncjcjlcchrrftrrjccrcrqqgcglcgcscmmlzmmtcmcffwfcfrcrggdmggdvvnrvnnphnngzzpdpgpspqqgrrnffmfpmffmgfmmjmzztlljlggljjcnnrqnqpnqndnffnwwbpwpjwjjlslmsmtmtjttsvsggrmmdpmmcjjswsqqwfwwrwffczfzggqvggdlldhllsdsfdsdhhmmzmjjmpjpddsccqrrjhjlhjjcnnpwnnffjwwcsszrrnmnsmnnjbnndwnnnhnwwjtwtlwtwqqbnqnbnqqfjfdjdbbwbqwqpqggbcbhhtrtqrrddpdwdlwdddzvzwvvdfdpdcdvdtdpttwwdzdzmdmqmzmnzmnmhmwmjwwshhcqcpcvvzgzdggnjnnhwnhhswwvccqrqlqggcngnmggmffblbglltlstshhrjjlvlppsqslljtjtgglvltvlvmllhrhdrrmqrmqmjjdcjjppqwwllvsvsrszslsvvghvvhmmfbfvfpfmmvdvppwggtrrjvvsbbzffbmffpqqqhnhncclzczwcwpwssrprfrsrbsbnbvnnwzwqqpsqsspmssztssstrtcczsznzvvpvttnssdjdhjddngdgvvmsszbzsbsmbbgsbgsgmmhwhghrhjhphshchgglmlvlhlbhlldwdggdsscvcbcssfbbvggvwwtstltrrwttjdtdvttlsttfhfmhhcbclbcbffqqslshlldhdqhhjwwlffrbrdbrrgcrrfmffbhhlslrslrslrlsrsnsnvvqfqnnfdfmfmttmcmcrrcmmmjttjvtvvjbjqjnnbtbnblbtlblplgltlqltlztzvvtdvvtpvvwdwfflbflfrrhbrbbmjmcjmccztzwwjzwwzwdzdnnwcclbllqgghjhlhthwrdglrmcpbmtrnrdtvjrpmzqmljzzrtpzsrhnjrsdmpnsgdhvqchcfqjqdncjqfnscwjqvszpzzfhpjljmvsqnjzmrsgsbzlvrddtdmwbwwgprlvdfflrpztdzrhtmlzrrtdmpmcprqzzwlnmfjvsrltfjgcnnfllnzmbjcbthvbffczsspmczrpgpdjmvrvfmprfmnqdcnfwwvgdrwvrbtlqmhrrjvtrmmgrlprtnzdlszgbtbwztdrmpmlfblshzcnsczlblgwzrpnlccwhmcqhssmpznbdnnqgzzmjprjttdjhmjbmgqvzblsjwmplzsthrswhsdbvtqgrfzmbpqtpqgqdqcvzlgjrtvrhvzgmcmrwdmfpdvjddsmmsnvrdgnsbsdzcbprbqchqcgnwmfsrmqtrcdhdtzztbvmpblftwqlmlmmjcjhhjlgnnhljnncvbnjhgbjrltlwscswgvqmcnssbcdrtbgnhgmpmvjwtrbrbrdbdqfrncvhdstwztwcpbjrjwzmdlwvlvmsrhghjwjnjstbcqjqtjrgcvhzjdhdgbgdlhvjmztwvhgzzggwwhhhzvtrldchztmwfjvnqnvhnwpfvzzvnlvsccmvsngzgtnttssmdmhwzlhtpnfhczsdfnrstbwvwpqmslcvpvhfzttzhsgzpbhqdtswshljpncznjhzmgvvbcllmzprhrvwljwcjpcdqmwbzvsdcgtmwnrhswsgqhwpwhbjpnhnpjvgsqcjltzrqvqfflcdcvpwnznvtqbfbtlpmtdgbbwdwncqsqnbtgfdzzqzzvjnwmzdmlgstmnjwznjqghglvmwjzlqrnddcqhgndlhlbmqdhrqgrjqztnhpzssnwmrqclmwpgbvfrvgqqvtthznsqwgndjrprbgrhcvhpzbfhdmgnhsrqjvjstbtmnltsbjfzczvjqnhtldqclsflbhvvlzjwrqqgbgpwqwpfjctqpzdqwcfstmwbzgrgrtzngljjnvtggrqcbgjwtqsdgwmfjqppnzgfsfdmlctztbhnntnntdlvrsdvnllvmpggjzspqfhzwrttwzpqrnqjhmpjnmrzrpnqzshcqgctbtflqflcrzpmnphgbbghhwzplljwngbtffwmrwggdztvtfgwldlswqvjptvbfvnbpglhgrdgcfmvrslqldmwjqvjpvwgpjddvglllvpqwvbchqsmjrncgvgmqbsbcwfbsbpqcqzjfpcdzszgmvqgqjlflpfzbsrhsrzrdbpssrjbcfhvztftlzqpsglpwhbscgwdlbgghzsbwznnbgnnsgjghmmpmmrmqmdhnflgvgprqfcbpzbcpjscvnpfrmtvzsbflmffvcfsvdsggzdqtppcjzphcqwrqtrczqmwcdmdqndzmhdpnfqsbndnvjlzrsjzmpcrfgjwccsdtzvslccwhlvzjwjgvwpsnsggmqgsjfbwmjstsgnqmtjhljvfnflnngdrqvscwlqqdsglhghczhjdvgrjcqblmncdbjvsbwgptgpvvzhcjgjnvttrgzrjnqlvfbrmpzdcbbnnrqptpzpssznbsrstdphbgdrsnrhcjwwgsncdzvqfnmnvqcmcgdgjdbqjzdrvvbvhjdfcqndmqwscmsvppclzrhgbldqtwctbdhpbbwfvwpcpsvddmrhqbhlrrmrblnmqqqbwvcwwbwprlmhtdncmjhmjgphmrrhcdrqgmcrzwsznqzpngbtsvjgglrddhjflbrhvqwmmhmqzhphwnvqwzczdvqjsnlhfqbcgddtwgnlcgbfqmzfpqmnbpvfhdhjlnwtrlmggtbfnfvmqrzjvjjvffctsrwgfcpghhnzqmwtlsfhjrvqpwqhngrhpswslsvtgnbvbmwsfwmpntfsfpshrjzvghhpvnlbmnrhltfpmqdwzfhztvhlmbnmhnbvdzbbtczvwbvwtvjghhjjrtgbrqrhmbgvssstdwztdmdsqtctghjhsnpslqttdlvndmjfnmdzwrblfjqcwptfttvlcgsvwcbmfzbdlmrtchgqlfspwznbzfjthjtfwshqgfsfdsmzsmpptzschlzjshvfwtmpszvrvlggbrgpcnqwndhjjprztdfddblhfljbvttfvhchhdfsftrhccrbncmhwpcpwfqthngcqptmvsmpcswdrdlcbqvvhwmcqqwbzlblrgfcrrndwdvlvnpjvwchzjzmgrqhzzmgqqdsdflpclpdtlhvhcthzjfbvjvzsnbvwfsnglvbnwnbgrqwpbgclhjhztttbjwvmlmmgmzncbwswncqhmcfjfnwnpbrmchhpgwngrfwgdfdqmblwlghdjvdhjftdblrtcvvgbvpmbjhfwgpmghqbqrcpgfvhtvqtlbjdblggcpjzlrhpbsqwntfhbhwwszpdlsgbpfqhvrjrhsldcgvqhqmwdfcrcmhrvvwvbrfsrrcvwzhqqvgltlnhwhdrhrdqsvmdzjwgmqdsccwhcgwltfhdfqpsltjccwsttmrc"