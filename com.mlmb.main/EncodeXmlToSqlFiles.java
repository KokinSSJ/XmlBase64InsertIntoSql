import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EncodeXmlToSqlFiles {
	
	
	private static String pathToXml = "./XMLfolder";
	private static String pathToSql = "./SQLfolder";
	
	public static void main(String[] args) throws IOException {
		// all xml files
		Stream<Path> inputXML = Files.list(Paths.get(pathToXml)).filter(t -> t.toString().toLowerCase().endsWith(".xml"));

		// map key = path to sql file / value = base64 of xml
		Map<Path, String> map = inputXML.collect(Collectors.toMap(i-> i, i -> Base64.getEncoder().encodeToString(encodeXmlToBase64String(i))));
		System.out.println(map);
		// ENTRYSET!!
		// existed sql files from xml files list
		Stream<Path> sqlFiles = map.keySet().stream().map(path -> getFileNameWithoutExtension(path))
				.map(EncodeXmlToSqlFiles::buildPathString)
				.map(Paths::get).map(path -> checkIfFileExist(path));

		// sql bodies filtered by "decode=('"
		Stream<List<String>> filesFiltered = sqlFiles.map(EncodeXmlToSqlFiles::readAllLinesToList)
				// .map(el -> el.stream().reduce((x, y) -> x + "\n" +
				// y).orElse(""))
				.filter(EncodeXmlToSqlFiles::matchLine);
		// x, y -> x + "\n" + y
		// filesFiltered.forEach(EncodeXmlToSqlFiles::matchLine2);


		// System.out.println(.collect(Collectors.toList()));
//		.map(Base64.getEncoder()::encodeToString);
		
	}

	private static void matchLine2(String line, Map.Entry<Path, String> row) {
		int baseStart = line.indexOf("decode=('");
		int baseStop  = line.indexOf("'", baseStart);
		String newFile = line.substring(0, baseStart) + row.getValue() + line.substring(baseStop);
		// try {
		//// FileUtils.writeStringToFile(row.getKey().toFile(), newFile,
		// "UTF-8", false);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	private static boolean matchLine(List<String> lines) {
		return lines.stream().anyMatch(line -> line.contains("decode=('"));
	}

	private static List<String> readAllLinesToList(Path t) {
		try {
			return Files.readAllLines(t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	private static Path checkIfFileExist(Path path) {
		if(path.toFile().exists()) {
			return path;
		}
		System.out.println("File: " + path.toFile().getName() + " doesn't exists");
		return null;
	}
	public static String buildPathString(String fileName) {
		return pathToSql + "/" + fileName + ".sql";
	}

	private static String getFileNameWithoutExtension(Path path) {
		String name = path.toFile().getName();
		return name.substring(0, name.indexOf(".xml"));
	}
	
	@FunctionalInterface
	interface SupplierEncode<T> {
		T get(Path path) throws IOException;
	}

	private static <T> T getFileBody(SupplierEncode<T> supplier, Path path) {
		try {
			return supplier.get(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] encodeXmlToBase64String(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
