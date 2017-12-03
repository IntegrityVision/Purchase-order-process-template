/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.com.integrity.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MSExcel {

    private String filePath;
    private FileInputStream fis;
    private XSSFWorkbook book;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public FileInputStream getFis() {
        return fis;
    }

    public void setFis(FileInputStream fis) {
        this.fis = fis;
    }

    public XSSFWorkbook getBook() {
        return book;
    }

    public void setBook(XSSFWorkbook book) {
        this.book = book;
    }

    public MSExcel(String filePath) {
        try {
            this.fis = new FileInputStream(new File(filePath));
            this.book = new XSSFWorkbook(fis);
        } catch (IOException ex) {
            Logger.getLogger(MSExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<List<String>> getData(int sheetNumber) {
        List<List<String>> data = new ArrayList<>();
        XSSFSheet sheet = book.getSheetAt(sheetNumber);
        for (Row row : sheet) {
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                rowData.add(getAsString(cell));
            }
            data.add(rowData);
        }
        return data;
    }

    private String getAsString(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case STRING:
                return cell.getStringCellValue();
            default:
                return "";
        }
    }

}
