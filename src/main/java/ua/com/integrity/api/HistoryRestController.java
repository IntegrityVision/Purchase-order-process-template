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

package ua.com.integrity.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.persistence.entity.CommentEntity;
import org.camunda.bpm.engine.task.Comment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ua.com.integrity.camunda.ResourcesAuthorization;
import ua.com.integrity.model.User;
import ua.com.integrity.service.orgstructure.OrgStructure;

@RestController
@RequestMapping("api")
public class HistoryRestController {

    @Resource
    private OrgStructure orgStructure;

    @RequestMapping(value = "/{taskId}/comment", method = RequestMethod.GET)
    @ResponseBody
    public List<Comment> getComments(@PathVariable(value = "taskId") String taskId) {
        ProcessEngine processEngine = ResourcesAuthorization.lookupProcessEngine(ResourcesAuthorization.ENGINE_NAME);
        List<Comment> result = null;
        if (processEngine != null) {
            TaskService taskService = processEngine.getTaskService();
            String processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
            result = taskService.getProcessInstanceComments(processInstanceId);
            for (HistoricTaskInstance task : processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list()) {
                result.addAll(taskService.getTaskComments(task.getId()));
            }
        }
        return result.stream().map(comment -> format(comment)).collect(Collectors.toList());
    }

    private Comment format(Comment comment) {
        Comment resultComment;
        if (comment instanceof CommentEntity) {
            CommentEntity commentEntity = (CommentEntity) comment;
            String userId = comment.getUserId();
            Optional<User> user = orgStructure.getUsers().stream().filter(userF -> userF.getEmail().equals(userId)).findFirst();
            String userName = user.isPresent() ? user.get().getName() : userId;
            commentEntity.setUserId(userName);
            resultComment = commentEntity;
        } else {
            resultComment = comment;
        }
        return resultComment;
    }

}
