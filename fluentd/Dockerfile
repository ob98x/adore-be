FROM fluent/fluentd:edge-debian
ADD config/fluentd.conf /fluentd/etc/fluentd.conf
USER root
RUN gem install fluent-plugin-elasticsearch --no-document
USER fluent