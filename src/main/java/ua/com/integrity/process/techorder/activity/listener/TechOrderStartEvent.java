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

package ua.com.integrity.process.techorder.activity.listener;

import javax.annotation.Resource;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import ua.com.integrity.model.Letter;
import ua.com.integrity.model.techorder.TechOrder;
import ua.com.integrity.service.LetterTemplateService;
import ua.com.integrity.service.MailService;
import ua.com.integrity.service.orgstructure.OrgStructureHelper;

@Component
public class TechOrderStartEvent implements ExecutionListener {

    @Resource
    private OrgStructureHelper orgStructureHelper;

    @Resource
    private MailService mailService;

    @Resource
    private LetterTemplateService letterTemplateService;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        String userId = delegateExecution.getProcessEngineServices().getIdentityService().getCurrentAuthentication().getUserId();
        String processInstanceId = delegateExecution.getProcessInstanceId();
        TechOrder techOrder = (TechOrder) delegateExecution.getVariable("techorder");
        techOrder.setId(processInstanceId);
        techOrder.setOwner(orgStructureHelper.getUser(userId));
        delegateExecution.setVariable("processReaders", userId);
        Letter letter = letterTemplateService.generateStartForTechOrder(techOrder.getId());
        mailService.sendMail(userId, letter.getSubject(), letter.getBody());
    }

}
