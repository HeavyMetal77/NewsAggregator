package ua.tarastom.newsaggregator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import ua.tarastom.newsaggregator.models.Article;
import ua.tarastom.newsaggregator.utils.Utils;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {

    private List<Article> articles;
    private OnItemClickListener onItemClickListener;
    private Context context;
    private OnReachEndListener onReachEndListener;

    public ArticleAdapter(Context context) {
        this.articles = new ArrayList<>();
        this.context = context;
    }

    public interface OnReachEndListener {
        void onReachEnd();
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles.addAll(articles);
        notifyDataSetChanged();
    }

    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_list, parent, false);
        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        Article article = articles.get(position);

        if (articles.size() >= 20 && position > articles.size() - 5 && onReachEndListener != null) {
            onReachEndListener.onReachEnd();
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();
        requestOptions.timeout(3000);

        Glide.with(context)
                .load(article.getEnclosure().get(0))
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);//TODO hide progressBar if load img failed
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.img_news);

        holder.title.setText(article.getTitle());
        if (article.getTitleChannel() != null) {
            holder.author.setText(article.getTitleChannel());
        }
        holder.description.setText(article.getDescription());
        holder.source.setText(article.getSource());
        holder.publishedAt.setText(Utils.DateToTimeFormat(article.getPubDate()));
        String textTime = "\u2022 " + Utils.DateToTimeFormat(article.getPubDate());
        holder.time.setText(textTime);
    }

    @Override
    public int getItemCount() {
        Log.d("ArticleAdapter", "getItemCount()" + articles.size());
        return articles.size();
    }

    class ArticleHolder extends RecyclerView.ViewHolder {
        TextView title, description, author, publishedAt, source, time;
        ImageView img_news;
        ProgressBar progressBar;

        public ArticleHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.desc);
            author = itemView.findViewById(R.id.author);
            publishedAt = itemView.findViewById(R.id.publishedAt);
            source = itemView.findViewById(R.id.source);
            time = itemView.findViewById(R.id.time);
            img_news = itemView.findViewById(R.id.img_news);
            progressBar = itemView.findViewById(R.id.progressBar_load_photo);

            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }
}
