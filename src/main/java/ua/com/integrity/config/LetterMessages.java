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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "locales/letter_messages.properties", encoding = "UTF-8")
public class LetterMessages {

    @Value("${commonSubject}")
    private String commonSubject;
    @Value("${commonBody}")
    private String commonBody;
    @Value("${techorder.commonSubject}")
    private String toCommonSubject;
    @Value("${techorder.commonBody}")
    private String toCommonBody;
    @Value("${techorder.startSubject}")
    private String toStartSubject;
    @Value("${techorder.startBody}")
    private String toStartBody;
    @Value("${techorder.doneSubject}")
    private String toDoneSubject;
    @Value("${techorder.doneBody}")
    private String toDoneBody;
    @Value("${techorder.reminderSubject}")
    private String toReminderSubject;
    @Value("${techorder.reminderBody}")
    private String toReminderBody;

    public String getCommonSubject() {
        return commonSubject;
    }

    public String getCommonBody() {
        return commonBody;
    }

    public String getToCommonSubject() {
        return toCommonSubject;
    }

    public String getToCommonBody() {
        return toCommonBody;
    }

    public String getToStartSubject() {
        return toStartSubject;
    }

    public String getToStartBody() {
        return toStartBody;
    }

    public String getToDoneSubject() {
        return toDoneSubject;
    }

    public String getToDoneBody() {
        return toDoneBody;
    }

    public String getToReminderSubject() {
        return toReminderSubject;
    }

    public String getToReminderBody() {
        return toReminderBody;
    }

}
