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

package ua.com.integrity.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("process.ou")
public class PositionsNames {

    private String hrPosition;
    private String omPosition;
    private List<String> itPositions;
    private String pmPosition;
    private String generalManagerPosition;
    private String deputyGeneralManagerPosition;
    private List<String> accountantPositions;
    private String mainAccountantPosition;
    private List<String> supportPositions;
    private String itDepHeadPosition;

    public PositionsNames() {
        itPositions = new ArrayList<>();
        accountantPositions = new ArrayList<>();
        supportPositions = new ArrayList<>();
    }

    public String getHrPosition() {
        return hrPosition;
    }

    public void setHrPosition(String hrPosition) {
        this.hrPosition = hrPosition;
    }

    public String getOmPosition() {
        return omPosition;
    }

    public void setOmPosition(String omPosition) {
        this.omPosition = omPosition;
    }

    public List<String> getItPositions() {
        return itPositions;
    }

    public void setItPositions(List<String> itPositions) {
        this.itPositions = itPositions;
    }

    public String getPmPosition() {
        return pmPosition;
    }

    public void setPmPosition(String pmPosition) {
        this.pmPosition = pmPosition;
    }

    public String getGeneralManagerPosition() {
        return generalManagerPosition;
    }

    public void setGeneralManagerPosition(String generalManagerPosition) {
        this.generalManagerPosition = generalManagerPosition;
    }

    public String getDeputyGeneralManagerPosition() {
        return deputyGeneralManagerPosition;
    }

    public void setDeputyGeneralManagerPosition(String deputyGeneralManagerPosition) {
        this.deputyGeneralManagerPosition = deputyGeneralManagerPosition;
    }

    public List<String> getAccountantPositions() {
        return accountantPositions;
    }

    public void setAccountantPositions(List<String> accountantPositions) {
        this.accountantPositions = accountantPositions;
    }

    public String getMainAccountantPosition() {
        return mainAccountantPosition;
    }

    public void setMainAccountantPosition(String mainAccountantPosition) {
        this.mainAccountantPosition = mainAccountantPosition;
    }

    public List<String> getSupportPositions() {
        return supportPositions;
    }

    public void setSupportPositions(List<String> supportPositions) {
        this.supportPositions = supportPositions;
    }

    public String getItDepHeadPosition() {
        return itDepHeadPosition;
    }

    public void setItDepHeadPosition(String itDepHeadPosition) {
        this.itDepHeadPosition = itDepHeadPosition;
    }

}
