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

package ua.com.integrity.process.techorder.activity;

import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.task.IdentityLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.integrity.model.Letter;
import ua.com.integrity.model.techorder.TechOrder;
import ua.com.integrity.service.LetterTemplateService;
import ua.com.integrity.service.MailService;

@Component
public class TechOrderNotification {

    @Autowired
    private LetterTemplateService letterTemplateService;

    @Resource
    private MailService mailService;

    public void sendMailToExecutors(DelegateTask delegateTask) {
        TechOrder techOrder = (TechOrder) delegateTask.getVariable("techorder");
        Letter letter = letterTemplateService.generateCommonForTechOrder(techOrder.getId(), delegateTask.getId());
        String assignee = delegateTask.getAssignee();
        if ((assignee != null) && (!assignee.isEmpty())) {
            mailService.sendMail(assignee, letter.getSubject(), letter.getBody());
        } else {
            Set<String> candidates = delegateTask.getCandidates().stream().map(IdentityLink::getUserId).collect(Collectors.toSet());
            for (String candidate : candidates) {
                mailService.sendMail(candidate, letter.getSubject(), letter.getBody());
            }
        }
    }

}
