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

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ua.com.integrity.config.LetterMessages;
import ua.com.integrity.model.Letter;

@Component
public class LetterTemplateService {

    @Resource
    private LetterMessages letterMessages;

    @Value("${camunda.host}")
    private String host;

    public Letter generateCommon(String appId, String taskId) {
        String subject = String.format(letterMessages.getCommonSubject(), appId);
        String body = String.format(letterMessages.getCommonBody(), host, taskId, appId);
        return new Letter(subject, body);
    }

    public Letter generateCommonForTechOrder(String appId, String taskId) {
        String subject = String.format(letterMessages.getToCommonSubject(), appId);
        String body = String.format(letterMessages.getToCommonBody(), host, taskId, appId);
        return new Letter(subject, body);
    }

    public Letter generateStartForTechOrder(String appId) {
        String subject = String.format(letterMessages.getToStartSubject(), appId);
        String body = letterMessages.getToStartBody();
        return new Letter(subject, body);
    }

    public Letter generateDoneForTechOrder(String appId) {
        String subject = String.format(letterMessages.getToDoneSubject(), appId);
        String body = letterMessages.getToDoneSubject();
        return new Letter(subject, body);
    }

    public Letter generateReminderForTechOrder(String appId, String taskId) {
        String subject = String.format(letterMessages.getToReminderSubject(), appId);
        String body = String.format(letterMessages.getToReminderSubject(), host, taskId, appId);
        return new Letter(subject, body);
    }

}
