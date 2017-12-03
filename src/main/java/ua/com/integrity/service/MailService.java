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

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${camunda.mailTo}")
    private String testMail;

    public void sendMail(String mailTo, String mailSubject, String mailText, Attachment attachment) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            if (attachment != null) {
                helper.addAttachment(attachment.getFileName(), attachment.getInputStreamSource(), attachment.getContentType());
            }
            helper.setTo(!"".equals(testMail) ? testMail : mailTo);
            helper.setSubject(mailSubject);
            helper.setText(mailText, true);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Email sending error: " + e.getMessage());
        }
    }

    public void sendMail(String mailTo, String mailSubject, String mailText) {
        sendMail(mailTo, mailSubject, mailText, null);
    }

    public static class Attachment {
        private String fileName;
        private InputStreamSource inputStreamSource;
        private String contentType;

        public Attachment(String fileName, InputStreamSource inputStreamSource, String contentType) {
            this.fileName = fileName;
            this.inputStreamSource = inputStreamSource;
            this.contentType = contentType;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public InputStreamSource getInputStreamSource() {
            return inputStreamSource;
        }

        public void setInputStreamSource(InputStreamSource inputStreamSource) {
            this.inputStreamSource = inputStreamSource;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }

}
