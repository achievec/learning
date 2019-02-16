# -*- coding: utf-8 -*-

from collections import defaultdict

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html
from dateutil.parser import parse

started_repos = []


class GithubStarsPipeline(object):

    def process_item(self, item, spider):
        item['name'] = item['name'][1:]
        item['update_time'] = parse(item['update_time']).strftime("%Y-%m-%d %H:%M:%m")
        if item['language'] is not None:
            item['language'] = item['language'].strip()
        started_repos.append(item)
        # print(item)
        return item

    def close_spider(self, spider):
        started_repos.sort(key=sort_by_stars_key_extractor, reverse=True)
        with open('achievec_stars.md', 'wb') as outfile:
            # json.dump(self.stars, outfile)
            for repo in started_repos:
                outfile.write(create_md_string(repo))
        # print(self.stars)


class GithubStarsJavaPipeline(object):

    def process_item(self, item, spider):
        return item

    def close_spider(self, spider):
        java_started_repos = list(
            filter(lambda repo: repo['language'] is not None and repo['language'] == 'Java', started_repos))
        java_started_repos.sort(key=sort_by_stars_key_extractor, reverse=True)
        with open('achievec_stars_java.md', 'wb') as outfile:
            for repo in java_started_repos:
                outfile.write(create_md_string(repo))
        # print(self.stars)


class GithubStarsGroupByLanguagePipeline(object):

    def process_item(self, item, spider):
        return item

    def close_spider(self, spider):
        language_to_repos_map = group_by_language(started_repos)
        with open('achievec_stars_group_by_language.md', 'wb') as outfile:
            for language, repos in language_to_repos_map.items():
                outfile.write(crate_language_header(language))
                for repo in repos:
                    outfile.write(create_md_string(repo))


def sort_by_stars_key_extractor(repo):
    return repo['stars']


def group_by_language(repos):
    language_to_repos_map = defaultdict(list)
    for repo in repos:
        if repo['language'] is None:
            language_to_repos_map['Others'].append(repo)
        else:
            language_to_repos_map[repo['language']].append(repo)

    for language, repos in language_to_repos_map.items():
        repos.sort(key=sort_by_stars_key_extractor, reverse=True)
    return language_to_repos_map


def create_md_string(repo):
    return u'''### [{repo[name]}]({repo[url]})
{repo[desc]}
- stars: {repo[stars]}
- language: {repo[language]}
- update time: {repo[update_time]}

'''.format(repo=repo).encode('utf-8')


def crate_language_header(language):
    return u'''
---
# *{language}*
'''.format(language=language).encode('utf-8')
